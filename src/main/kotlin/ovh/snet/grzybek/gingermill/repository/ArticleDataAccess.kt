package ovh.snet.grzybek.gingermill.repository

import ovh.snet.grzybek.gingermill.model.ArticleConnection
import ovh.snet.grzybek.gingermill.model.CurrentPosition
import ovh.snet.grzybek.gingermill.model.UntrackedPath

interface ArticleDataAccess {

  fun saveArticle(title: String)

  fun clearArticles()

  fun clearConnections()

  fun getPath(start: Int, end: Int): UntrackedPath?

  fun saveConnection(articleConnection: ArticleConnection)

  fun getCurrentPosition() : CurrentPosition

  fun getLongestPath() : Int

  fun savePosition(start: Int, end: Int)

  fun getPositionByTitle(title: String): Int
}