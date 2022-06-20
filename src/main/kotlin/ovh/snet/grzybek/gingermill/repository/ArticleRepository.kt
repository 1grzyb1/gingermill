package ovh.snet.grzybek.gingermill.repository

import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.data.neo4j.repository.query.Query
import org.springframework.data.repository.query.Param
import ovh.snet.grzybek.gingermill.model.ArticleEntity

interface ArticleRepository : Neo4jRepository<ArticleEntity?, String?> {
  fun findByTitle(title: String): ArticleEntity?

  @Query("MATCH (n:Article) where n.visited is null return n limit 1")
  fun findFirstUnvisited(): ArticleEntity?

  @Query("MERGE (n:Article {title: $0}) return n")
  fun mergeLinkedArticles(title: String) : ArticleEntity

  @Query(
    "MATCH\n" +
        "  (a:Article),\n" +
        "  (b:Article)\n" +
        "WHERE a.title = $0 AND b.title in $1\n" +
        "CREATE (a)-[r:LINKS_TO]->(b)\n"
  )
  fun createRelationBetween(
    @Param("parentName") parentName: String,
    @Param("childName") childName: List<String>
  )

  @Query(
    "MATCH (a:Article),\n" +
        "      (b:Article),\n" +
        "      p = shortestPath((a)-[*]->(b))\n" +
        "  where (a <> b)\n" +
        "RETURN p\n" +
        "  order by length(p) desc\n" +
        "  limit 1"
  )
  fun findLongestShortestPath(): ArticleEntity

  @Query(
    "MATCH (n)\n" +
        "DETACH DELETE n\n"
  )
  fun clear()
}