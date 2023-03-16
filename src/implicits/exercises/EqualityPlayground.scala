package implicits.exercises

import implicits.TypeClasses.User

object EqualityPlayground extends App:  
  trait Equality[T]:
    def equals(v1:T, v2: T): Boolean 

  implicit object UserComparison extends Equality[User]:
    override def equals(v1: User, v2: User): Boolean = v1.name == v2.name

  object Equality:
    def apply[T](a: T, b: T)(implicit eq: Equality[T]): Boolean = eq.equals(a,b)
  
  val user = User("John", 33)
  val user2 = User("John", 90)
  println(Equality(user, user2)) // ad-hoc polymorphism 

  // type safe implicit conversion class
  implicit class EqualityEnrichment[T](value: T):
    def ===(that: T)(implicit comparison: Equality[T]): Boolean = comparison.equals(value, that)
    def !==(that: T)(implicit comparison: Equality[T]): Boolean = !comparison.equals(value, that)
  println(user === user2)

  // type safety: user === 42 won't compile 

