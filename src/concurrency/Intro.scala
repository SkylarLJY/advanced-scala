package concurrency

import java.util.concurrent.Executor
import java.util.concurrent.Executors

object Intro extends App{
  /* JVM threads */
  val aThread = new Thread(new Runnable {
    override def run(): Unit = println("new thread running...")
  })

  aThread.start() // creates a JVM thread that runs on an OS thread
  aThread.join() // block until the thread done running 

  // executors to reuse thread b/c they are expansive to create & destory 
  val pool = Executors.newFixedThreadPool(10)
  pool.execute(()=>println("thread pool thread runnnig"))

  // pool.shutdown() to shut down in the main thread and stop taking in executions 
  pool.shutdownNow() // affects the running threads

}
