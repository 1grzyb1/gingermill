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

@Service
class CalculatePathService(
  private val articleDataAccess: ArticleDataAccess,
  private val articleRepository: ArticleRepository,
  private val logger: KLogger
) {

  fun calculatePaths() {
    runBlocking {
      val producer = produceUntrackedPath()
      repeat(5) { finShortestPath(it, producer) }
    }
  }

  fun CoroutineScope.produceUntrackedPath() = produce {
    while (true) {
      val paths = articleDataAccess.findUntrackedPath()
      paths.forEach {
        delay(10L)
        send(it)
      }
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