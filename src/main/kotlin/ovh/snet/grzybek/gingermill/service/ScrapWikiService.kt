package ovh.snet.grzybek.gingermill.service

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.springframework.stereotype.Service
import ovh.snet.grzybek.gingermill.model.Article
import java.util.regex.Pattern


@Service
class ScrapWikiService(
  private val pattern: Pattern = Pattern.compile(
    "<a href=\"/wiki/(.*?)\"",
    Pattern.CASE_INSENSITIVE
  ),
  private val client: OkHttpClient = OkHttpClient()
) {

  fun scrapArticle(title: String): Article {
    val url = String.format(
      "https://pl.wikipedia.org/wiki/%s",
      title
    )

    val request: Request = Request.Builder()
      .url(url)
      .build()

    val response: Response
    try {
       response = client.newCall(request).execute()
    } catch (e: Exception) {
      return Article(title)
    }

    val content = response.body?.string()

    var links = listOf<String>()
    val matcher = pattern.matcher(content)
    while (matcher.find()) {
      val link = matcher.group(1)
      if (link.contains(":")) {
        continue
      }
      links = links.plus(link)
    }

    return Article(title, links.map { Article(it) })
  }
}