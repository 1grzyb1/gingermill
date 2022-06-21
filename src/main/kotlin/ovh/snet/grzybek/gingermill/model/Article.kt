package ovh.snet.grzybek.gingermill.model

data class Article(val title: String, val links: List<Article> = listOf()) {

  fun getDepth(depth: Int = 0): Int{
    if (links.size == 0) {
      return depth
    }

    return links.get(0).getDepth(depth + 1)
  }
}