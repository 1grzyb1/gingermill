package ovh.snet.grzybek.gingermill.repository

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import ovh.snet.grzybek.gingermill.model.ArticleConnection
import ovh.snet.grzybek.gingermill.model.UntrackedPath

@Service
internal class JdbcArticleDataAccess(private val jdbcTemplate: JdbcTemplate) : ArticleDataAccess {

  private val objectMapper = ObjectMapper()

  private final val SAVE_QUERY = """
      INSERT INTO article(title)
      VALUES (?)
      ON CONFLICT DO NOTHING 
  """

  private final val CLEAR_QUERY = """
      DELETE FROM article WHERE TRUE
  """

  private final val CLEAR_CONNECTIONS_QUERY = """
      DELETE FROM article_connection WHERE TRUE
  """

  private final val UNTRACKED_PATH_QUERY = """
      WITH start_articles
      AS (SELECT * FROM article),
      end_articles AS (SELECT * FROM article)
  SELECT *
  FROM start_articles a
           JOIN end_articles b ON a.title != b.title
  WHERE (a.title, b.title) NOT IN (SELECT start, "end" FROM article_connection)
  LIMIT 1000
    """

  private final val SAVE_CONNECTION_QUERY = """
      INSERT INTO article_connection (start, "end", path, length) 
      VALUES (?, ?, ?::json, ?)
  """

  override fun saveArticle(title: String) {
    jdbcTemplate.update(SAVE_QUERY, title)
  }

  override fun clearArticles() {
    jdbcTemplate.update(CLEAR_CONNECTIONS_QUERY)
    jdbcTemplate.update(CLEAR_QUERY)
  }

  override fun findUntrackedPath(): MutableList<UntrackedPath> {
    return jdbcTemplate.query(
      UNTRACKED_PATH_QUERY
    ) { rs, _ ->
      UntrackedPath(rs.getString(1), rs.getString(2))
    }
  }

  override fun saveConnection(articleConnection: ArticleConnection) {
    jdbcTemplate.update(
      SAVE_CONNECTION_QUERY,
      articleConnection.start,
      articleConnection.end,
      objectMapper.writeValueAsString(articleConnection.path),
      articleConnection.length
    )
  }
}