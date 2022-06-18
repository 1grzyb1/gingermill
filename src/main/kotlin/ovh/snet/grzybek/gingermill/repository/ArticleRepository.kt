package ovh.snet.grzybek.gingermill.repository

import org.springframework.data.neo4j.repository.Neo4jRepository
import ovh.snet.grzybek.gingermill.model.ArticleEntity

interface ArticleRepository : Neo4jRepository<ArticleEntity?, String?> {

  fun findByTitle(title: String) : ArticleEntity?
}