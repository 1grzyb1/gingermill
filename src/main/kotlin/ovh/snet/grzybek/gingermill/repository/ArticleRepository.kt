package ovh.snet.grzybek.gingermill.repository

import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.data.neo4j.repository.query.Query
import ovh.snet.grzybek.gingermill.model.ArticleEntity

interface ArticleRepository : Neo4jRepository<ArticleEntity?, String?> {

  fun findByTitle(title: String) : ArticleEntity?

  fun findFirstByVisitedEquals(visited: Boolean) : ArticleEntity?

  @Query("MATCH (a:Article),\n" +
      "      (b:Article),\n" +
      "      p = shortestPath((a)-[*]->(b))\n" +
      "  where (a <> b)\n" +
      "RETURN p\n" +
      "  order by length(p) desc\n" +
      "  limit 1")
  fun findLongestShortestPath() : ArticleEntity

  @Query("MATCH (n)\n" +
      "DETACH DELETE n\n")
  fun clear()
}