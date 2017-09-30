package futures.v2_exceptions

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

/**
  * This example (not included in the book) shows what
  * happens when a Future throws an exception.
  *
  * Notice that the `delta` in the Failure block is
  * significantly less than `f1`’s sleep time. The exception
  * makes `f1`’s result unimportant.
  *
  */
object V1ExceptionsAndFutures extends App {

    val startTime = currentTime

    val f1 = Future {
        sleep(2000)
        1
    }
    val f2 = Future {
        sleep(550)
        throw new Exception("Ka-boom!")   //EXCEPTION
        2
    }
    val f3 = Future {
        sleep(800)
        3
    }

    val result = for {
        r2 <- f2
        r1 <- f1
        r3 <- f3
    } yield (r1 + r2 + r3)

    result.onComplete {
        case Success(x) => {
            // the code won't come here
            val finishTime = currentTime
            val delta = finishTime - startTime
            System.err.println(s"delta = $delta")
            println(s"\nresult = $x")
        }
        case Failure(e) => {
            // the code comes here because of the intentional exception
            val finishTime = currentTime
            val delta = finishTime - startTime
            System.err.println("Failure happened!")
            System.err.println(s"delta in Failure = $delta")
            // just a short message; i don't care about the full exception
            System.err.println(e.getMessage)
        }
    }

    // important for a little parallel demo: keep the main thread of the
    // jvm alive
    sleep(4000)

    def sleep(time: Long) = Thread.sleep(time)
    def currentTime = System.currentTimeMillis()

}


