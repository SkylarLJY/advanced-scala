package afp.exercises

import scala.annotation.tailrec

/* exercise: singly linked stream */
abstract class MyStream[+A]:
  def isEmpty: Boolean
  def head: A
  def tail: MyStream[A]

  def #::[B>:A](elem: B): MyStream[B] // prepend
  def ++[B>:A](anotherStream: =>MyStream[B]): MyStream[B]

  def foreach(f: A=>Unit): Unit
  def map[B](f: A=>B): MyStream[B]
  def flatMap[B](f: A=>MyStream[B]): MyStream[B]
  def filter(f: A=>Boolean): MyStream[A]

  def take(n: Int): MyStream[A]
  def takeAsList(n: Int): List[A] = take(n).toList()

  @tailrec
  final def toList[B>:A](acc: List[B]=Nil): List[B] = 
    if isEmpty then acc.reverse
    else tail.toList(head :: acc)

end MyStream

object MyStream:
  def from[A](i: A)(f: A=>A): MyStream[A] =
    new NonEmptyStream(i, MyStream.from(f(i))(f))

object EmptyStream extends MyStream[Nothing]:
  def isEmpty: Boolean = true
  def head: Nothing = throw new NoSuchElementException
  def tail: MyStream[Nothing] = throw new NoSuchElementException

  def #::[B>:Nothing](elem: B): MyStream[B] = new NonEmptyStream(elem, this)
  def ++[B>:Nothing](anotherStream: =>MyStream[B]): MyStream[B] = anotherStream

  def foreach(f: Nothing=>Unit): Unit = ()
  def map[B](f: Nothing=>B): MyStream[B] = this
  def flatMap[B](f: Nothing=>MyStream[B]): MyStream[B] = this
  def filter(f: Nothing=>Boolean): MyStream[Nothing] = this

  def take(n: Int): MyStream[Nothing] = this
end EmptyStream

class NonEmptyStream[A](hd: A, tl: =>MyStream[A]) extends MyStream[A]:
  def isEmpty: Boolean = false
  override val head: A = hd
  override lazy val tail: MyStream[A] = tl // call by name + lazy eval = call by need

  def #::[B>:A](elem: B): MyStream[B] = new NonEmptyStream(elem, this)
  def ++[B>:A](anotherStream: =>MyStream[B]): MyStream[B] = new NonEmptyStream(head, tail ++ anotherStream)

  def foreach(f: A=>Unit): Unit =
    f(head)
    tail.foreach(f)
  def map[B](f: A=>B): MyStream[B] = new NonEmptyStream(f(head), tail.map(f))
  def flatMap[B](f: A=>MyStream[B]): MyStream[B] = f(head) ++ tail.flatMap(f)
  def filter(f: A=>Boolean): MyStream[A] =
    if f(head) then new NonEmptyStream(head, tail.filter(f))
    else tail.filter(f)

  def take(n: Int): MyStream[A] =
    if n<=0 then EmptyStream
    else new NonEmptyStream(head, tail.take(n-1))

end NonEmptyStream

object StreamPlayground extends App:
  val naturals = MyStream.from(1)(i=>i+1)
  naturals.take(3).foreach(println)
  0 #:: naturals map (_*2) take 3 foreach println
  println()
  println(naturals.flatMap(x=> new NonEmptyStream(x, new NonEmptyStream(x+1, EmptyStream))).take(5).toList())
  println(naturals.filter(_<10).take(8).toList())

  // Fibonacci
  def constructFib(n1: Int, n2: Int): MyStream[Int] =
    new NonEmptyStream(n1+n2, constructFib(n2, n1+n2))
  val fib = constructFib(0,1)
  println(fib.take(5).toList())
  // prime numbers with Eratosthene's sieve
  val base = MyStream.from(2)(i=>i+1) 
  def sieve(in: MyStream[Int]): MyStream[Int] = 
    val num = in.head
    new NonEmptyStream(in.head, sieve(in.tail.filter(_%num!=0)))
    
  
  val sieved = sieve(base)
  println(sieved.take(10).toList())
