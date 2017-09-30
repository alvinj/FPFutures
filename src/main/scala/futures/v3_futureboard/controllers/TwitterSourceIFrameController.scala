package futures.v3_futureboard.controllers

import javax.swing.JInternalFrame

import futures.v3_futureboard._
import futures.v3_futureboard.utils.{NetworkUtils, SwingUtils, TwitterUtils}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}


class TwitterSourceIFrameController (twitterSource: TwitterSource)
extends IFrameControllerInterface {

    val iFrame = new JInternalFrame(twitterSource.title, true, true, true, true)

    def updateContent(): Unit = {

        val tweets: Future[Seq[String]] = Future {
            // (1) get the html
            val html = NetworkUtils.get(twitterSource.url, 5000, 5000)
            // (2) get the tweets as a Seq
            TwitterUtils.extractTweets(html)
        }

        //TODO these `invokeLater` calls shouldn’t be necessary, but they are
        tweets.onComplete {
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

}
