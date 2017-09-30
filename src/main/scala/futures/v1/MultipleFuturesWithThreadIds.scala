package futures.v1

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

/**
  * sample output:
  *
  * f1 start:          1
  * f2 start:          1
  * before for:        1
  * f3 start:          1
  * after for:         2
  * before onComplete: 2
  * after onComplete:  2
  * start sleep(2000): 2
  * in Success case:   1219
  *
  * result = (1206,6)
  * onComplete tid: 114
  *
  * Thread IDs
  * ----------
  * Main Thread ID: 125
  * F1 Thread ID:   114
  * F2 Thread ID:   112
  * F3 Thread ID:   113
  *
  */
object MultipleFuturesWithThreadIds extends App {

    val mainThreadId = Thread.currentThread.getId
    var f1ThreadId = 0L
    var f2ThreadId = 0L
    var f3ThreadId = 0L

    val startTime = currentTime

    /**
      * (a) create the futures. as you'll see in the
      * time-related output, they start running immediately.
      */
    val f1: Future[Int] = Future {
        println(s"f1 start:          ${deltaTime(startTime)}")
        f1ThreadId = Thread.currentThread.getId
        sleep(1200)
        1
    }
    val f2: Future[Int] = Future {
        println(s"f2 start:          ${deltaTime(startTime)}")
        f2ThreadId = Thread.currentThread.getId
        sleep(400)
        2
    }
    val f3: Future[Int] = Future {
        println(s"f3 start:          ${deltaTime(startTime)}")
        f3ThreadId = Thread.currentThread.getId
        sleep(800)
        3
    }

    // (b) merge the results when they become available
    println(s"before for:        ${deltaTime(startTime)}")
    val result: Future[(Long, Int)] = for {
        r1 <- f1
        r2 <- f2
        r3 <- f3
    } yield (deltaTime(startTime), r1 + r2 + r3)
    println(s"after for:         ${deltaTime(startTime)}")

    /**
      * the f1/f2/f3 println statements show that those code
      * blocks are started immediately. but because they’re
      * on different threads, the time-related println statements
      * in this (main) thread show that the main thread goes
      * flying right through the for-expression.
      */

    // (c) handle the result as a side effect
    println(s"before onComplete: ${deltaTime(startTime)}")
    result.onComplete {
        case Success(x) => {

            // sleep to show that for’s `yield` expression
            // happens just before this point
            sleep(10)

            // the “in success” time should be almost exactly the
            // same as the longest sleep time, plus the 10ms delay
            // above; approximately 1210ms for my sample times.
            val tInSuccessCase = deltaTime(startTime)
            println(s"in Success case:   ${tInSuccessCase}")
            println(s"\nresult = $x")
            println(s"onComplete tid: ${Thread.currentThread.getId}")

        }
        case Failure(e) => e.printStackTrace
    }
    println(s"after onComplete:  ${deltaTime(startTime)}")

    // important for a small parallel demo: keep the main jvm
    // thread alive
    println(s"start sleep(2000): ${deltaTime(startTime)}")
    sleep(2000)

    println("")
    println("Thread IDs")
    println("----------")
    println(s"Main Thread ID: ${mainThreadId}")
    println(s"F1 Thread ID:   ${f1ThreadId}")
    println(s"F2 Thread ID:   ${f2ThreadId}")
    println(s"F3 Thread ID:   ${f3ThreadId}")


    def sleep(time: Long) = Thread.sleep(time)
    def currentTime = System.currentTimeMillis()
    def deltaTime(t0: Long) = System.currentTimeMillis() - t0

}


