package ovh.snet.grzybek.gingermill.service

import com.gargoylesoftware.htmlunit.BrowserVersion
import com.gargoylesoftware.htmlunit.WebClient
import com.gargoylesoftware.htmlunit.html.HtmlPage
import org.jsoup.Jsoup
import org.springframework.stereotype.Service
import ovh.snet.grzybek.gingermill.model.Article
import java.util.regex.Pattern


@Service
class ScrapWikiService(
  private val pattern: Pattern = Pattern.compile(
    "<a href=\"/wiki/(.*?)\"",
    Pattern.CASE_INSENSITIVE
  )
) {

  fun scrapArticle(title: String): Article {
    val url = String.format(
      "https://pl.wikipedia.org/wiki/%s",
      title
    )

    val webClient = setUpWebClient()
    val htmlPage: HtmlPage = webClient?.getPage(url) ?: throw RuntimeException("Page no found")
    val parsedDocument = Jsoup.parse(htmlPage.asXml())
    val content = parsedDocument.select("#content").toString()

    var links = listOf<String>()
    val matcher = pattern.matcher(content)
    while (matcher.find()) {
      links = links.plus(matcher.group(1))
    }

    return Article(title, links.map { Article(it) })
  }

  private fun setUpWebClient(): WebClient? {
    val webClient = WebClient(BrowserVersion.FIREFOX)
    webClient.options.isThrowExceptionOnScriptError = false
    webClient.options.isThrowExceptionOnFailingStatusCode = false
    return webClient
  }
}