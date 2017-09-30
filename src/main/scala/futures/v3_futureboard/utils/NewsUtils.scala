package futures.v3_futureboard.utils

import org.apache.commons.lang3.StringEscapeUtils
import org.htmlcleaner.HtmlCleaner

import scala.collection.mutable.ListBuffer

object NewsUtils {

    def getUniqueHeadlinesFromHtmlString(
        htmlAsString: String,
        rootNodeToLookFor: String,
        attributeByName: String,
        whatToLookForInsideAttribute: String
    ): List[String] =
    {
        val stories = getAllMatchingHeadlinesFromHtmlString(
            htmlAsString,
            rootNodeToLookFor,
            attributeByName,
            whatToLookForInsideAttribute
        )
        val articles = getUniqueArticles(stories)
        articles.toList
    }

    // rootNodeToLookFor = "h2"
    // attributeByName = "class"  (class or id)
    // whatToLookForInsideAttribute = "headline"
    private def getAllMatchingHeadlinesFromHtmlString(
        htmlAsString: String,
        rootNodeToLookFor: String,
        attributeByName: String,
        whatToLookForInsideAttribute: String
    ): List[String] =
    {
        val cleaner = new HtmlCleaner
        val rootNode = cleaner.clean(htmlAsString)
        val elements = rootNode.getElementsByName(rootNodeToLookFor, true)
        var headlines = new ListBuffer[String]
        for (elem <- elements) {
            val classOrIdText: String = elem.getAttributeByName(attributeByName)
            if (classOrIdText != null && classOrIdText.contains(whatToLookForInsideAttribute)) {
                val text = StringEscapeUtils.unescapeHtml4(elem.getText.toString).trim
                headlines += text
            }
        }
        headlines.toList
    }

    private def getUniqueArticles(articles: List[String]): Set[String] = articles.toSet

    def filterHeadlines(headlines: Seq[String], topicsOfInterest: Seq[String]): Seq[String] = {
        headlines.filter(h => storyContainsDesiredPhrase(h, topicsOfInterest))
    }

    private def storyContainsDesiredPhrase(s: String, topics: Seq[String]): Boolean = {
        for (t <- topics) {
            if (s.contains(t)) return true
        }
        false
    }

}
