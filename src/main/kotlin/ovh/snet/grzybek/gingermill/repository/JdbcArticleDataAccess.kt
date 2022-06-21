package ovh.snet.grzybek.gingermill.repository

import org.intellij.lang.annotations.Language
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Service
import ovh.snet.grzybek.gingermill.model.UntrackedPath

@Service
internal class JdbcArticleDataAccess(private val jdbcTemplate: JdbcTemplate) : ArticleDataAccess {

  private final val SAVE_QUERY = """
      INSERT INTO article(title)
      VALUES (?)
      ON CONFLICT DO NOTHING 
  """

  private final val CLEAR_QUERY = """
      DELETE FROM article WHERE TRUE
  """


  private final val UNTRACKED_PATH_QUERY = """
    SELECT a.title AS start, b.title AS end
    FROM article a
             JOIN article b ON a.title != b.title
    WHERE (a.title, b.title) NOT IN (SELECT start, "end" FROM article_connection)
    LIMIT 1
  """

  override fun saveArticle(title: String) {
    jdbcTemplate.update(SAVE_QUERY, title)
  }

  override fun clearArticles() {
    jdbcTemplate.update(CLEAR_QUERY)
  }

  override fun findUntrackedPath(): UntrackedPath? {
    return jdbcTemplate.queryForObject(
      UNTRACKED_PATH_QUERY
    ) { rs, _ ->
      UntrackedPath(rs.getString(1), rs.getString(2))
    }
  }
}