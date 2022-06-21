package ovh.snet.grzybek.gingermill.repository

import ovh.snet.grzybek.gingermill.model.ArticleConnection
import ovh.snet.grzybek.gingermill.model.UntrackedPath

interface ArticleDataAccess {

  fun saveArticle(title: String)

  fun clearArticles()

  fun findUntrackedPath(): UntrackedPath?

  fun saveConnection(articleConnection: ArticleConnection)
}