package ovh.snet.grzybek.gingermill.rest

import org.springframework.web.bind.annotation.*
import ovh.snet.grzybek.gingermill.service.ArticleService
import ovh.snet.grzybek.gingermill.service.CalculatePathService
import ovh.snet.grzybek.gingermill.service.IndexWikipediaService


@RestController
@RequestMapping("/article")
class ArticleController
  (
  private val articleService: ArticleService,
  private val indexWikiService: IndexWikipediaService,
  private val calculatePathService: CalculatePathService
) {

  @DeleteMapping
  fun clear() {
    articleService.clear()
  }

  @GetMapping
  fun getArticles() {
    calculatePathService.calculatePaths()
  }

  @PutMapping
  fun addArticle() {
    indexWikiService.indexWikipedia();
  }
}