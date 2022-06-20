package ovh.snet.grzybek.gingermill.model

import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Property
import org.springframework.data.neo4j.core.schema.Relationship

@Node("Article")
class ArticleEntity(
  @Id val title: String,
  @Property("visited") var visited: Boolean = false,
) {

  @Relationship(type = "LINKS_TO", direction = Relationship.Direction.OUTGOING)
  var articles: Set<ArticleEntity> = HashSet()

  fun toArticle(): Article {
    return Article(title, articles.map { it.toArticle() })
  }

  companion object {
    fun fromArticle(article: Article, visited: Boolean = false): ArticleEntity {
      return ArticleEntity(article.title, visited)
    }
  }
}