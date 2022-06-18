package ovh.snet.grzybek.gingermill.model

data class Article(val title: String, val links: List<Article>)