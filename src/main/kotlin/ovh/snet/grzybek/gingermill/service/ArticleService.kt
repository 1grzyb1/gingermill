package ovh.snet.grzybek.gingermill.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ovh.snet.grzybek.gingermill.model.Article
import ovh.snet.grzybek.gingermill.model.ArticleEntity
import ovh.snet.grzybek.gingermill.repository.ArticleRepository

@Service
class ArticleService(private val articleRepository: ArticleRepository) {

  fun getArticles(): List<Article?> {
    return articleRepository.findAll().map { it?.toArticle() }
  }

  @Transactional
  fun getUnvisitedArticle(): Article? {
    val unvisited = articleRepository.findFirstByVisitedEquals(false)
      ?: throw RuntimeException("Did not find unvisited Node")
    unvisited.visited = true
    articleRepository.save(unvisited)
    return unvisited.toArticle()
  }

  fun getLongestShortestPath(): Article {
    return articleRepository.findLongestShortestPath().toArticle()
  }

  fun clear() {
    articleRepository.clear()
  }

  @Transactional
  fun saveArticle(article: Article) {
    val articleEntity =
      articleRepository.findByTitle(article.title) ?: ArticleEntity.fromArticle(article)
    articleEntity.visited = true
    val linkedArticles = getLinkedArticles(article)
    articleEntity.articles = linkedArticles.toSet()
    articleRepository.save(articleEntity)
  }

  private fun getLinkedArticles(article: Article): List<ArticleEntity> {
    return article.links
      .filter { it.title != article.title }
      .map {
        articleRepository.findByTitle(it.title) ?: ArticleEntity.fromArticle(it)
      }
  }
}