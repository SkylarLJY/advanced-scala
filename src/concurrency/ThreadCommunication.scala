package concurrency

import scala.collection.mutable
import java.util.Random

object ThreadCommunication extends App{
  /* producer-consumer problem */
  // Synchronized expression: lock on the object - only AnyRef can be sychronized 
  // wait() - release lockand wait
  // notify() - wake up one sleeping thread, the lock on monitor is only released after the synchronized block 
  class SimpleContainer:
    private var value: Int = 0
    def get(): Int = 
      val res = value
      value = 0
      res
    def set(newVal: Int): Unit = 
      value = newVal
  
  def prodCons(): Unit = 
    val container = new SimpleContainer
    val consumer = new Thread(()=>{
      container.synchronized {
        println("[consumer] waiting...")
        container.wait()
        println("[consumer] got value "+container.get())
      }
    })

    val producer = new Thread(()=>{
      container.synchronized {
        println("[producer] Producing value...")
        Thread.sleep(2000)
        container.set(20)
        container.notify()
      }
    })

    consumer.start()
    producer.start()
  
  // prodCons()

  // producer-consumer with a buffer (multiple values)
  def bufferProdCons(): Unit =
    val buffer: mutable.Queue[Int] = new mutable.Queue[Int]
    val capacity = 3

    val consumer = new Thread(()=>{
      val random = new Random()
      while true do
        buffer.synchronized {
          if buffer.isEmpty then buffer.wait()
          val res = buffer.dequeue()
          println("consumer got " + res)
          buffer.notify()
        }
        Thread.sleep(random.nextInt(500))
    })

    val producer = new Thread(()=>{
      val random = new Random()
      var i = 0 
      while true do
        buffer.synchronized {
          if buffer.size == capacity then buffer.wait()
          buffer.enqueue(i)
          println("producer produced " + i)
          i += 1
          buffer.notify()
        }
        Thread.sleep(random.nextInt(500))
    })

    consumer.start()
    producer.start()
  
  // bufferProdCons()

  // multiple producers and consumers on the same buffer 
}
