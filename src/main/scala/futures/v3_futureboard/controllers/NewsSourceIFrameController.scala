package futures.v3_futureboard.controllers

import javax.swing._

import futures.v3_futureboard._
import futures.v3_futureboard.utils.{NetworkUtils, NewsUtils, SwingUtils}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

class NewsSourceIFrameController(newsSource: NewsSource)
extends IFrameControllerInterface {

    val iFrame = new JInternalFrame(newsSource.title, true, true, true, true)

    /**
      * this method updates the ListModel on the Swing EDT with
      * SwingUtils.invokeLater, so it doesn’t have to be called
      * from the EDT
      */
    def updateContent(): Unit = {
        val headlines: Future[Seq[String]] = Future {
            getHeadlines(
                newsSource.url,
                newsSource.rootNodeToLookFor,
                newsSource.attributeByName,
                newsSource.whatToLookForInsideAttribute,
                newsSource.phrasesOfInterest
            )
        }

        headlines.onComplete {
            case Success(res) => {
                SwingUtils.invokeLater {
                    listModel.clear()
                    res.foreach(x => listModel.addElement(x))
                }
            }
            case Failure(ex) => {
                val msg = "ka-boom: " + ex.getMessage
                SwingUtils.invokeLater {
                    listModel.clear()
                    listModel.addElement(msg)
                }
            }
        }

    }

    private def getHeadlines(
        url: String,
        rootNodeToLookFor: String,
        attributeByName: String,
        whatToLookForInsideAttribute: String,
        phrasesOfInterest: Seq[String]
    ): Seq[String] = {

        // (1) get the html
        val htmlAsString = NetworkUtils.get(url, 5000, 5000)

        // (2) parse the html
        val rawHeadlines: List[String] = NewsUtils.getUniqueHeadlinesFromHtmlString(
            htmlAsString,
            rootNodeToLookFor,
            attributeByName,
            whatToLookForInsideAttribute
        )

        // (3) filter those headlines and return the result
        NewsUtils.filterHeadlines(rawHeadlines, phrasesOfInterest)

    }

}















