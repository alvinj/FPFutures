package futures.v3_futureboard.utils

import org.apache.commons.lang3.StringEscapeUtils
import org.htmlcleaner.HtmlCleaner

object TwitterUtils {

    /**
      * @param html The HTML of a Twitter page as a String
      * @return A list of the individual tweets from that page.
      */
    def extractTweets(html: String): Seq[String] = {
        val cleaner = new HtmlCleaner
        val rootNode = cleaner.clean(html)
        val elements = rootNode.getElementsByName("div", true)
        val tweetsSeq: Seq[String] = for {
            e <- elements
            currentClass = e.getAttributeByName("class")
            if currentClass != null
            if currentClass.contains("tweet-text") // a css class twitter uses on each tweet
            tweetText = StringEscapeUtils.unescapeHtml4(e.getText.toString.trim)
        } yield tweetText
        tweetsSeq
    }

}
