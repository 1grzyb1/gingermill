package ovh.snet.grzybek.gingermill.service

import org.springframework.data.neo4j.core.Neo4jTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ovh.snet.grzybek.gingermill.model.Article
import ovh.snet.grzybek.gingermill.model.ArticleEntity
import ovh.snet.grzybek.gingermill.repository.ArticleRepository

@Service
class ArticleService(
  private val articleRepository: ArticleRepository,
  private val neo4jTemplate: Neo4jTemplate
) {

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

    var start = System.currentTimeMillis()
    val saved = neo4jTemplate.save(articleEntity)
    var end = System.currentTimeMillis()
    println("Saved parent in: ${end - start}")

    start = System.currentTimeMillis()
    saved.articles = linkedArticles.toSet()
    saved.articles.forEach { articleRepository.createRelationBetween(saved.title, it.title) }
    end = System.currentTimeMillis()
    println("Saved relations in: ${end - start}")

  }

  private fun getLinkedArticles(article: Article): List<ArticleEntity> {
    val articlesWithoutParent = article.links.filter { it.title != article.title }
    val existingArticles =
      articleRepository.findByTitleIn(articlesWithoutParent.map { it.title })
    val existingTitles = existingArticles.map { it.title }

    val newArticles = articlesWithoutParent
      .filter { !existingTitles.contains(it.title) }
      .map { ArticleEntity.fromArticle(it) }
      .distinctBy { it.title }

    val start = System.currentTimeMillis()
    neo4jTemplate.saveAll(newArticles)
    val end = System.currentTimeMillis()
    println("Saved children in: ${end - start}")

    return existingArticles.plus(newArticles)
  }
}