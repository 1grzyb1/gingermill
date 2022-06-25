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

  private val startPosition = AtomicInteger(-1)
  private val endPosition = AtomicInteger(-1)
  private val longestPath = AtomicInteger(-1)

  fun calculatePaths() {
    setInitialValues()

    runBlocking {
      val producer = produceUntrackedPath()
      repeat(2) { finShortestPath(it, producer) }
    }
  }

  fun CoroutineScope.produceUntrackedPath() = produce {
    while (true) {
      if (endPosition.get() == startPosition.get()) {
        endPosition.incrementAndGet()
      }

      val path =
        articleDataAccess.getPath(startPosition.get(), endPosition.getAndIncrement())

      if (path == null) {
        startPosition.incrementAndGet();
        endPosition.set(0)
        continue
      }

      send(path)
    }
  }

  fun CoroutineScope.finShortestPath(id: Int, channel: ReceiveChannel<UntrackedPath>) = launch {
    for (path in channel) {
      logger.info { "Finding shortest path between ${path.start} and ${path.end} using thread $id" }
      withContext(Dispatchers.IO) {
        val shortestEntity =
          articleRepository.findShortestPath(path.start, path.end) ?: ArticleEntity("-1")
        val shortest = shortestEntity.toArticle()
        logger.info { "Found shortest path between ${path.start} and ${path.end} using thread $id" }

        if (shortest.getDepth() > longestPath.get()) {
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

  private fun setInitialValues() {
    val currentPosition = articleDataAccess.getCurrentPosition()
    startPosition.set(currentPosition.start)
    endPosition.set(currentPosition.end)
    longestPath.set(articleDataAccess.getLongestPath())
  }

}