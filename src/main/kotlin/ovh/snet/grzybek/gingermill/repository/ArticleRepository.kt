package ovh.snet.grzybek.gingermill.repository

import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.data.neo4j.repository.query.Query
import org.springframework.data.repository.query.Param
import ovh.snet.grzybek.gingermill.model.ArticleEntity

interface ArticleRepository : Neo4jRepository<ArticleEntity?, String?> {

  @Query("MATCH (n:Article) where n.visited is null return n limit 1")
  fun findFirstUnvisited(): ArticleEntity?

  @Query("MERGE (n:Article {title: $0}) return n limit 1")
  fun mergeArticle(title: String): ArticleEntity

  @Query(
    "MATCH (p:Article {title: $0})\n" +
        "SET p.visited = true\n" +
        "RETURN p limit 1"
  )
  fun updateUnvisitedNode(title: String): ArticleEntity

  @Query(
    """
      MATCH
      (a:Article),
      (b:Article)
      WHERE a.title = $0 AND b.title in $1 AND a.title <> b.title
      CREATE (a)-[r:LINKS_TO]->(b)
    """
  )
  fun createRelationBetween(
    @Param("parentName") parentName: String,
    @Param("childName") childName: List<String>
  )

  @Query(
    "MATCH (a:Article {title: $0}),\n" +
        "      (b:Article {title: $1}),\n" +
        "      p = shortestPath((a)-[*]->(b))\n" +
        "  where (a <> b)\n" +
        "RETURN p\n" +
        "  order by length(p) desc\n" +
        "  limit 1"
  )
  fun findShortestPath(start: String, end: String): ArticleEntity?

  @Query(
    """
      MATCH (source:Article{title: $0})
      CALL gds.bfs.stream('articles_graph', {
        sourceNode: source
      })
      YIELD path
      RETURN last(nodes(path))
      LIMIT 1
    """
  )
  fun findFarthest(title: String): ArticleEntity?

  @Query(
    "MATCH (n) DETACH DELETE n"
  )
  fun clear()

  @Query(
    """
      CALL gds.graph.project('articles_graph', 'Article', 'LINKS_TO') YIELD graphName
    """
  )
  fun createGraph()

  @Query(
    """
      CALL gds.graph.drop('articles_graph') YIELD graphName
    """
  )
  fun removeGraph()
}