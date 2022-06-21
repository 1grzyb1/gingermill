package ovh.snet.grzybek.gingermill.repository

import org.intellij.lang.annotations.Language
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service

@Service
internal class JdbcArticleDataAccess(private val jdbcTemplate: JdbcTemplate) : ArticleDataAccess {

  @Language("postgres")
  private final val SAVE_QUERY = """
      INSERT INTO article(title)
      VALUES (?)
      ON CONFLICT DO NOTHING 
  """

  @Language("postgres")
  private final val CLEAR_QUERY = """
      DELETE FROM article WHERE true
  """

  override fun saveArticle(title: String) {
    jdbcTemplate.update(SAVE_QUERY, title)
  }

  override fun clearArticles() {
    jdbcTemplate.update(CLEAR_QUERY)
  }
}