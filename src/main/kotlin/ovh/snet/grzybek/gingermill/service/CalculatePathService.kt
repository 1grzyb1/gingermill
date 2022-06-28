package ovh.snet.grzybek.gingermill.service

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import mu.KLogger
import org.springframework.stereotype.Service
import ovh.snet.grzybek.gingermill.model.ArticleConnection
import ovh.snet.grzybek.gingermill.model.ArticleEntity
import ovh.snet.grzybek.gingermill.model.UntrackedPath
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
  private val endPosition = AtomicInteger(1)
  private val longestPath = AtomicInteger(0)

  fun calculatePaths(startTitle: String?, endTitle: String?, moveStart: Boolean) {

    setInitialValues(startTitle, endTitle)

    GlobalScope.launch {
      val producer = produceUntrackedPath(moveStart)
      repeat(10) { finShortestPath(it, producer) }
    }

    GlobalScope.launch {
      savePosition()
    }
  }

  fun CoroutineScope.produceUntrackedPath(moveStart: Boolean) = produce {
    while (true) {
      if (moveStart) {
        startPosition.incrementAndGet()
      }
      else {
        endPosition.incrementAndGet()
      }

      if (endPosition.get() == startPosition.get()) {
        continue
      }

      val path =
        articleDataAccess.getPath(startPosition.get(), endPosition.get()) ?: break

      send(path)
    }
  }

  fun CoroutineScope.finShortestPath(id: Int, channel: ReceiveChannel<UntrackedPath>) = launch {
    for (path in channel) {
      logger.info { "Finding shortest path between ${path.start} and ${path.end}" }
      withContext(Dispatchers.IO) {
        val shortestEntity =
          articleRepository.findShortestPath(path.start, path.end) ?: ArticleEntity("-1")
        val shortest = shortestEntity.toArticle()
        logger.debug { "Found shortest path between ${path.start} and ${path.end}" }

        if (shortest.getDepth() > longestPath.get()) {
          longestPath.set(shortest.getDepth())
          articleDataAccess.clearConnections()
        }

        if (shortest.getDepth() >= longestPath.get()) {
          articleDataAccess.saveConnection(
            ArticleConnection(
              path.start,
              path.end,
              shortest,
              if (shortest.title != "-1") shortest.getDepth() else -1
            )
          )
        }
      }
    }
  }

  private fun setInitialValues(startTitle: String?, endTitle: String?) {

    if (startTitle != null) {
      startPosition.set(articleDataAccess.getPositionByTitle(startTitle))
    }

    if (endTitle != null) {
      endPosition.set(articleDataAccess.getPositionByTitle(endTitle))
    }

    longestPath.set(articleDataAccess.getLongestPath())
  }

  private suspend fun savePosition() {
    while (true) {
      articleDataAccess.savePosition(startPosition.get(), endPosition.get())
      delay(30000)
    }
  }
}