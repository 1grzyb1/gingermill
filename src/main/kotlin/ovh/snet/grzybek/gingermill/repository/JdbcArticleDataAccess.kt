package ovh.snet.grzybek.gingermill.repository

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import ovh.snet.grzybek.gingermill.model.ArticleConnection
import ovh.snet.grzybek.gingermill.model.CurrentPosition
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
         AS (SELECT title FROM article WHERE position = ?),
     end_articles AS (SELECT title FROM article WHERE position = ?)
SELECT *
FROM start_articles a
         JOIN end_articles b ON a.title != b.title
LIMIT 1
    """

  private final val SAVE_CONNECTION_QUERY = """
      INSERT INTO article_connection (start, "end", path, length) 
      VALUES (?, ?, ?::json, ?)
  """

  private final val CURRENT_POSITION_QUERY = """
      SELECT start, "end" FROM current_positions ORDER BY id DESC LIMIT 1
  """

  private final val LONGEST_PATH_QUERY = """
      SELECT length FROM article_connection ORDER BY length DESC LIMIT 1
  """

  private final val INSERT_POSITION_QUERY = """
      INSERT INTO current_positions(start, "end")
      VALUES (?, ?)
  """

  private final val GET_POSITION_QUERY = """
      SELECT position FROM article WHERE title = ?
  """

  override fun saveArticle(title: String) {
    jdbcTemplate.update(SAVE_QUERY, title)
  }

  override fun clearArticles() {
    jdbcTemplate.update(CLEAR_CONNECTIONS_QUERY)
    jdbcTemplate.update(CLEAR_QUERY)
  }

  override fun clearConnections() {
    jdbcTemplate.update(CLEAR_CONNECTIONS_QUERY)
  }

  override fun getPath(start: Int, end: Int): UntrackedPath? {
    return try {
      jdbcTemplate.queryForObject(UNTRACKED_PATH_QUERY, { rs, _ ->
        UntrackedPath(rs.getString(1), rs.getString(2))
      }, start, end)
    } catch (e: Exception) {
      return null
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

  override fun getCurrentPosition(): CurrentPosition {
    return jdbcTemplate.queryForObject(CURRENT_POSITION_QUERY) { rs, _ ->
      CurrentPosition(rs.getInt(1), rs.getInt(2))
    } ?: throw RuntimeException("Current position doesn't exists")
  }

  override fun getLongestPath(): Int {
    return try {
      jdbcTemplate.queryForObject(LONGEST_PATH_QUERY) { rs, _ ->
        rs.getInt(1)
      } ?: 0
    } catch (e: Exception) {
      0
    }
  }

  override fun savePosition(start: Int, end: Int) {
    jdbcTemplate.update(INSERT_POSITION_QUERY, start, end)
  }

  override fun getPositionByTitle(title: String): Int {
    return jdbcTemplate.queryForObject(GET_POSITION_QUERY, { rs, _ -> rs.getInt(1) }, title)
      ?: throw RuntimeException("Couldn't find title")
  }
}