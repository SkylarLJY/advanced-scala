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

  def -(elem: A): MySet[A]
  def &(anotherSet: MySet[A]): MySet[A]
  def --(anotherSet: MySet[A]): MySet[A]

  def unary_! : MySet[A]
end MySet

class EmptySet[A] extends MySet[A]:
  def contains(elem: A): Boolean = false
  def +(elem: A): MySet[A] = new NonEmptySet[A](elem, this)
  def ++(anotherSet: MySet[A]): MySet[A] = anotherSet

  def map[B](f: A => B): MySet[B] = new EmptySet[B]
  def flatMap[B](f: A => MySet[B]): MySet[B] = new EmptySet[B]
  def foreach(f: A => Unit): Unit = ()
  def filter(f: A => Boolean): MySet[A] = this

  def -(elem: A): MySet[A] = this
  def &(anotherSet: MySet[A]): MySet[A] = this
  def --(anotherSet: MySet[A]): MySet[A] = anotherSet
  
  def unary_! : MySet[A] = new PropertyBasedSet[A](_=>true)
end EmptySet

//class AllInclusiveSet[A] extends MySet[A]:
//  override def contains(elem: A): Boolean = true
//  override def &(anotherSet: MySet[A]): MySet[A] = filter(anotherSet)
//  override def ++(anotherSet: MySet[A]): MySet[A] = this
//  override def -(elem: A): MySet[A] = ???
//  override def --(anotherSet: MySet[A]): MySet[A] = filter(!anotherSet.contains(_))
//  override def filter(f: A => Boolean): MySet[A] = ??? // property based set
//  override def flatMap[B](f: A => MySet[B]): MySet[B] = ???
//  override def foreach(f: A => Unit): Unit = ???
//  override def map[B](f: A => B): MySet[B] = ???
//  override def unary_! : MySet[A] = new EmptySet[A]
//end AllInclusiveSet

// all elements satisfy the property 
class PropertyBasedSet[A](property: A=>Boolean) extends MySet[A]:
  override def contains(elem: A): Boolean = property(elem)
  override def &(anotherSet: MySet[A]): MySet[A] = filter(anotherSet)
  override def +(elem: A): MySet[A] = new PropertyBasedSet[A](x => property(x) || x==elem)
  override def ++(anotherSet: MySet[A]): MySet[A] = new PropertyBasedSet[A](x => property(x) && anotherSet.contains(x))
  override def -(elem: A): MySet[A] = filter(_!=elem)
  override def --(anotherSet: MySet[A]): MySet[A] = filter(!anotherSet)
  override def filter(f: A => Boolean): MySet[A] = new PropertyBasedSet[A](x => property(x) && f(x))

  // Don't know if mapping an inf set will result in an inf set or not
  override def flatMap[B](f: A => MySet[B]): MySet[B] = actionFail

  override def map[B](f: A => B): MySet[B] = actionFail
  override def foreach(f: A => Unit): Unit = actionFail
  
  override def unary_! : MySet[A] = new PropertyBasedSet[A](!property(_))

  def actionFail = throw new IllegalArgumentException("failed")
end PropertyBasedSet

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

  def -(elem: A): MySet[A] = if head == elem then tail else tail-elem + head
  def &(anotherSet: MySet[A]): MySet[A] = filter(anotherSet) // b/c apply() checks if contains
  def --(anotherSet: MySet[A]): MySet[A] = filter(!anotherSet.contains(_))

  def unary_! : MySet[A] = new PropertyBasedSet[A](x => !this.contains(x))
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

  val negS = !s
  println(negS(2))
  println(negS(9))
}