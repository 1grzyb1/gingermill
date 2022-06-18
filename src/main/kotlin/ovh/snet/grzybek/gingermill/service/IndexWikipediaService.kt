package ovh.snet.grzybek.gingermill.service

import mu.KLogger
import mu.KotlinLogging
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class IndexWikipediaService(
  private val articleService: ArticleService,
  private val scrapWikiService: ScrapWikiService
) {

  private val logger: KLogger = KotlinLogging.logger {}
  private var indexingCounter = 0

  @Async
  fun indexWikipedia() {
    articleService.clear()

    indexStartPage()

    while (true) {
      val unvisited = articleService.getUnvisitedArticle() ?: return
      logger.info { "indexing article: ${unvisited.title}, this is $indexingCounter article" }
      val article = scrapWikiService.scrapArticle(unvisited.title)
      articleService.saveArticle(article)
      indexingCounter++
    }
  }

  private fun indexStartPage() {
    val article = scrapWikiService.scrapArticle("")
    articleService.saveArticle(article)
  }
}