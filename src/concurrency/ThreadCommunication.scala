package concurrency

import scala.collection.mutable
import java.util.Random
import scala.collection.immutable.LazyList.cons
import javax.swing.plaf.metal.MetalTheme

object ThreadCommunication extends App:
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

    val consume: Runnable = ()=>{
      val random = new Random()
      while true do
        buffer.synchronized {
          while buffer.isEmpty do buffer.wait()
          val res = buffer.dequeue()
          println("consumer got " + res)
          buffer.notify()
        }
        Thread.sleep(random.nextInt(500))
    } 

    val produce: Runnable = () => {
      val random = new Random()
      var i = 0 
      while true do
        buffer.synchronized {
          while buffer.size == capacity do buffer.wait()
          buffer.enqueue(i)
          println("producer produced " + i)
          i += 1
          buffer.notify()
        }
        Thread.sleep(random.nextInt(500))
    }

    val consumer = new Thread(consume)

    val producer = new Thread(produce)


    (0 to 3).foreach(_=>{
      new Thread(consume).start()
      new Thread(produce).start()
      
    })
    
  
  bufferProdCons()

  // multiple producers and consumers on the same buffer 
  // can't control if it's notifying a consumer or a producer when either calls notify()->check after waking up 
  
