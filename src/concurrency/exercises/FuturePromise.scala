package concurrency.exercises

import scala.concurrent.Promise
import scala.concurrent.Await
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}
import scala.concurrent.ExecutionContext.Implicits.global

object FuturePromise extends App{
  // fulfill a future immediately 
  def immediateFulfil[T](value: T): Future[T] = Future(value)

  // fulfill in sequence
  def inSeq[A, B](fa: Future[A], fb: Future[B]): Future[B] = 
    fa.flatMap(_=>fb)

  // new future with the first return
  def first[T](fa: Future[T], fb: Future[T]): Future[T] = 
    val promise = Promise[T]()
    // a promise can only be fulfilled once
    // trying to fulfill again will throw error 

    def tryComplete(res: Try[T]) = res match
      case Success(r) => 
        try promise.success(r) 
        catch
          case _=>
      case Failure(ex) => 
        try promise.failure(ex)
        catch
          case _=>

    
    fa.onComplete(tryComplete)
    // promise has a try complete
    fb.onComplete(promise.tryComplete)

    promise.future

  def last[T](fa: Future[T], fb: Future[T]): Future[T] = 
    val p1 = Promise[T]()
    val p2 = Promise[T]()

    def checkComplete(res: Try[T]) = 
      if p1.isCompleted then p2.complete(res)
      else p1.complete(res)

    fa.onComplete(checkComplete)
    fb.onComplete(checkComplete)

    p2.future

  val fast = Future(12)
  val slow = Future {
    Thread.sleep(300)
    33
  }

  first(fast, slow).foreach(println)
  last(fast, slow).foreach(println)

  Thread.sleep(500)

  // retry until 
  def retryUntil[A](action: ()=>Future[A], cond: A=>Boolean): Future[A] = 
    action().filter(cond).recoverWith {
      case _: Throwable => retryUntil(action, cond)
    }
}
