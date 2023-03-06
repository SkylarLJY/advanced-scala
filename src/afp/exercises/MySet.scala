package afp.exercises

import scala.annotation.tailrec

trait MySet[A] extends (A=>Boolean):
  //need to implement an apply() as it extends a function 
  def apply(v1: A): Boolean = this.contains(v1) 
  def contains(elem: A): Boolean
  def +(elem: A): MySet[A]
  def ++(anotherSet: MySet[A]): MySet[A]

  def map[B](f: A=>B): MySet[B]
  def flatMap[B](f: A=>MySet[B]): MySet[B]
  def foreach(f: A=> Unit): Unit
  def filter(f: A=>Boolean): MySet[A]
end MySet

class EmptySet[A] extends MySet[A]:
  def contains(elem: A): Boolean = false
  def +(elem: A): MySet[A] = new NonEmptySet[A](elem, this)
  def ++(anotherSet: MySet[A]) = anotherSet

  def map[B](f: A => B): MySet[B] = new EmptySet[B]
  def flatMap[B](f: A => MySet[B]): MySet[B] = new EmptySet[B]
  def foreach(f: A => Unit): Unit = ()
  def filter(f: A => Boolean): MySet[A] = this
end EmptySet


class NonEmptySet[A](head: A, tail: MySet[A]) extends MySet[A]:
  def contains(elem: A): Boolean = head==elem || tail.contains(elem)
  def +(elem: A): MySet[A] = if contains(elem) then this else new NonEmptySet[A](elem, this)
  def ++(anotherSet: MySet[A]): MySet[A] = tail ++ anotherSet + head
  
  def map[B](f: A => B): MySet[B] = tail.map(f) + f(head)
  def flatMap[B](f: A => MySet[B]): MySet[B] = f(head) ++ tail.flatMap(f)
  def foreach(f: A => Unit): Unit =
    f(head)
    tail.foreach(f)
  def filter(f: A => Boolean): MySet[A] = 
    val filteredTail = tail.filter(f)
    if f(head) then filteredTail + head
    else filteredTail
end NonEmptySet

object MySet:
  def apply[A](values: A*): MySet[A] = 
    @tailrec
    def buildSet(seq: Seq[A], acc: MySet[A]): MySet[A] = 
      if seq.isEmpty then acc
      else buildSet(seq.tail, acc+seq.head)
    
    buildSet(values.toSeq, new EmptySet[A])
end MySet

object MySetPlayground extends App{
  val s = MySet(1,2,3)
  (s+4).filter(_%2==0).foreach(println)
}