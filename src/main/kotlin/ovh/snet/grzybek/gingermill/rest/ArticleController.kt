package ovh.snet.grzybek.gingermill.rest

import org.springframework.web.bind.annotation.*
import ovh.snet.grzybek.gingermill.model.Article
import ovh.snet.grzybek.gingermill.service.ArticleService
import ovh.snet.grzybek.gingermill.service.IndexWikipediaService
import ovh.snet.grzybek.gingermill.service.ScrapWikiService


@RestController
@RequestMapping("/article")
class ArticleController
  (
  private val articleService: ArticleService,
  private val scrapWikiService: ScrapWikiService,
  private val indexWikiService: IndexWikipediaService
) {

  @GetMapping
  fun getArticles(): List<Article?> {
//    return articleService.getArticles()
    return listOf(articleService.getUnvisitedArticle())
//    return listOf(articleService.getLongestShortestPath())
//    return listOf(scrapWikiService.scrapArticle("Doda_(piosenkarka)"))
  }

  @PutMapping
  fun addArticle(): Article? {
//    val article = Article("asd", listOf(Article("vvv", listOf())))
//    articleService.saveArticle(article)
//    return article
    indexWikiService.indexWikipedia();
    return articleService.getUnvisitedArticle()
  }

  @PostMapping
  fun getArticle(): Article? {
    articleService.clear()
    articleService.saveArticle(Article("1"))
    articleService.saveArticle(Article("2", listOf(Article("1"), Article("3"))))
    return Article("")
//    return scrapWikiService.scrapArticle("Maj_2005")
  }
}