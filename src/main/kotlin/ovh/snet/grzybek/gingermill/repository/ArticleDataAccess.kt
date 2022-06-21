package ovh.snet.grzybek.gingermill.repository

interface ArticleDataAccess {

  fun saveArticle(title: String)

  fun clearArticles()
}