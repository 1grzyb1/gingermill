package ovh.snet.grzybek.gingermill.service

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import mu.KLogger
import org.springframework.stereotype.Service
import ovh.snet.grzybek.gingermill.model.Article
import ovh.snet.grzybek.gingermill.repository.ArticleDataAccess
import java.util.concurrent.atomic.AtomicInteger

@Service
class IndexWikipediaService(
  private val articleService: ArticleService,
  private val scrapWikiService: ScrapWikiService,
  private val logger: KLogger,
  private val articleDataAccess: ArticleDataAccess
) {

  private var indexingCounter = AtomicInteger(0)

  fun indexWikipedia() {

    indexStartPage()

    runBlocking {
      val producer = produceArticle()
      repeat(10) { indexArticle(it, producer) }
    }
  }

  fun CoroutineScope.produceArticle() = produce {
    while (true) {
      val unvisited = articleService.getUnvisitedArticle() ?: continue
      send(unvisited)
    }
  }

  fun CoroutineScope.indexArticle(id: Int, channel: ReceiveChannel<Article>) = launch {
    for (unvisited in channel) {
      val article = scrapWikiService.scrapArticle(unvisited.title)
      logger.info { "indexing article: ${unvisited.title} with ${article.links.size}, this is $indexingCounter article with thread #$id" }
      withContext(Dispatchers.IO) {
        articleService.saveArticle(article)
        articleDataAccess.saveArticle(article.title)
      }
      indexingCounter.incrementAndGet()
    }
  }

  private fun indexStartPage() {
    val article = scrapWikiService.scrapArticle("")
    articleService.saveArticle(article)
  }
}