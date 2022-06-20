package ovh.snet.grzybek.gingermill.service

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mu.KLogger
import org.springframework.stereotype.Service
import ovh.snet.grzybek.gingermill.model.Article
import java.util.*

@Service
class IndexWikipediaService(
  private val articleService: ArticleService,
  private val scrapWikiService: ScrapWikiService,
  private val logger: KLogger
) {

  private var indexingCounter = 0
  private val toIndex: Queue<Article> = LinkedList<Article>()

  fun indexWikipedia() {
    articleService.clear()

    indexStartPage()

    GlobalScope.launch {
      addToQueue()
    }

    GlobalScope.launch {
      index()
    }
  }

  private fun addToQueue() {
    while (true) {
      val unvisited = articleService.getUnvisitedArticle() ?: continue
      toIndex.add(unvisited)
    }
  }

  private fun index() {
    while (true) {
      if (toIndex.isEmpty()) continue
      val unvisited = toIndex.remove() ?: continue
      val article = scrapWikiService.scrapArticle(unvisited.title)
      logger.info { "indexing article: ${unvisited.title} with ${article.links.size}, this is $indexingCounter article" }
      articleService.saveArticle(article)
      indexingCounter++
    }
  }

  private fun indexStartPage() {
    val article = scrapWikiService.scrapArticle("")
    articleService.saveArticle(article)
  }
}