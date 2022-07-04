package ovh.snet.grzybek.gingermill.service

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import mu.KLogger
import org.springframework.stereotype.Service
import ovh.snet.grzybek.gingermill.model.ArticleConnection
import ovh.snet.grzybek.gingermill.model.ArticleEntity
import ovh.snet.grzybek.gingermill.repository.ArticleDataAccess
import ovh.snet.grzybek.gingermill.repository.ArticleRepository
import java.util.concurrent.atomic.AtomicInteger

@Service
class CalculatePathService(
  private val articleDataAccess: ArticleDataAccess,
  private val articleRepository: ArticleRepository,
  private val logger: KLogger
) {

  private val startPosition = AtomicInteger(1)
  private val longestPath = AtomicInteger(0)

  fun calculatePaths(startTitle: String?) {

    setInitialValues(startTitle)

    GlobalScope.launch {
      val producer = produceUntrackedPath()
      repeat(20) { finShortestPath(it, producer) }
    }

    GlobalScope.launch {
      savePosition()
    }
  }

  fun CoroutineScope.produceUntrackedPath() = produce {
    while (true) {
      startPosition.incrementAndGet()

      val path =
        articleDataAccess.getTitleByPosition(startPosition.get()) ?: break

      send(path)
    }
  }

  fun CoroutineScope.finShortestPath(id: Int, channel: ReceiveChannel<String>) = launch {
    for (startArticle in channel) {
      logger.info { "Finding farthest article for ${startArticle}" }
      withContext(Dispatchers.IO) {
        val farthestEntity =
          articleRepository.findFarthest(startArticle) ?: ArticleEntity("-1")
        logger.debug { "Found farthest path between ${startArticle}" }

        val pathEntity =
          articleRepository.findShortestPath(startArticle, farthestEntity.title)
        val path = pathEntity?.toArticle() ?: return@withContext

        if (path.getDepth() > longestPath.get()) {
          longestPath.set(path.getDepth())
          articleDataAccess.clearConnections()
        }

        if (path.getDepth() >= longestPath.get()) {
          articleDataAccess.saveConnection(
            ArticleConnection(
              startArticle,
              farthestEntity.title,
              path,
              if (farthestEntity.title != "-1") path.getDepth() else -1
            )
          )
        }
      }
    }
  }

  private fun setInitialValues(startTitle: String?) {

    try {
      articleRepository.removeGraph()
    } catch (e: Exception) {
      logger.warn { "Graph isn't present" }
    } finally {
      articleRepository.createGraph()
    }

    if (startTitle != null) {
      startPosition.set(articleDataAccess.getPositionByTitle(startTitle))
    }

    longestPath.set(articleDataAccess.getLongestPath())
  }

  private suspend fun savePosition() {
    while (true) {
      articleDataAccess.savePosition(startPosition.get())
      delay(30000)
    }
  }
}