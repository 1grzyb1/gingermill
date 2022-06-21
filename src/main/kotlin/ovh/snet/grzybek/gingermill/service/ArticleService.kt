package ovh.snet.grzybek.gingermill.service

import mu.KLogger
import org.springframework.data.neo4j.core.Neo4jTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ovh.snet.grzybek.gingermill.model.Article
import ovh.snet.grzybek.gingermill.model.ArticleEntity
import ovh.snet.grzybek.gingermill.repository.ArticleDataAccess
import ovh.snet.grzybek.gingermill.repository.ArticleRepository

@Service
class ArticleService(
  private val articleRepository: ArticleRepository,
  private val neo4jTemplate: Neo4jTemplate,
  private val logger: KLogger,
  private val articleDataAccess: ArticleDataAccess
) {

  @Transactional
  fun getUnvisitedArticle(): Article? {
    val unvisited = articleRepository.findFirstUnvisited() ?: return null
    unvisited.visited = true
    articleRepository.updateUnvisitedNode(unvisited.title)
    return unvisited.toArticle()
  }

  fun clear() {
    articleRepository.clear()
    articleDataAccess.clearArticles()
  }

  fun saveArticle(article: Article) {

    val linkedArticles = getLinkedArticles(article)
    val saved = saveParentArticle(article)

    createRelationships(saved, linkedArticles.toSet())
  }

  private fun createRelationships(saved: ArticleEntity, linkedArticles: Set<ArticleEntity>) {
    val start = System.currentTimeMillis()

    saved.articles = linkedArticles.toSet()
    articleRepository.createRelationBetween(saved.title, saved.articles.map { it.title })

    val end = System.currentTimeMillis()
    logger.info("Saved relations in: ${end - start}")
  }

  private fun saveParentArticle(article: Article): ArticleEntity {
    val start = System.currentTimeMillis()
    val articleEntity = ArticleEntity.fromArticle(article, true)
    val saved = neo4jTemplate.save(articleEntity)
    val end = System.currentTimeMillis()
    logger.info("Saved parent in: ${end - start}")

    return saved
  }

  private fun getLinkedArticles(article: Article): List<ArticleEntity> {
    val start = System.currentTimeMillis()
    val children = article.links.map {
      articleRepository.mergeLinkedArticles(it.title)
    }
    val end = System.currentTimeMillis()
    logger.info("Saved children in: ${end - start}")

    return children
  }
}