package ovh.snet.grzybek.gingermill.service

import org.springframework.stereotype.Service
import ovh.snet.grzybek.gingermill.model.Article
import ovh.snet.grzybek.gingermill.model.ArticleEntity
import ovh.snet.grzybek.gingermill.repository.ArticleRepository

@Service
class ArticleService(private val articleRepository: ArticleRepository) {

  fun getArticles() : List<Article?> {
    return articleRepository.findAll().map { it?.toArticle() }
  }

  fun getUnvisitedArticle() : Article? {
    return articleRepository.findFirstByVisitedEquals(false)?.toArticle()
  }

  fun getLongestShortestPath() : Article {
    return articleRepository.findLongestShortestPath().toArticle()
  }

  fun saveArticle(article: Article) {
    val articleEntity =
      articleRepository.findByTitle(article.title) ?: ArticleEntity.fromArticle(article)
    articleEntity.visited = true
    val linkedArticles = getLinkedArticles(article.links)
    articleEntity.articles = linkedArticles.toSet()
    articleRepository.save(articleEntity)
  }

  private fun getLinkedArticles(articles: List<Article>): List<ArticleEntity> {
    return articles.map {
      articleRepository.findByTitle(it.title) ?: articleRepository.save(
        ArticleEntity.fromArticle(it)
      )
    }
  }
}