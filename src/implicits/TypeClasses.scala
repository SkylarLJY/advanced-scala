package implicits

import javax.swing.text.html.HTML

object TypeClasses extends App{
  case class User(name: String, age: Int)
  val user = User("John", 33)

  trait HTMLSerializer[T]:
    def serialize(value: T): String

  implicit object UserSerializer extends HTMLSerializer[User]:
    override def serialize(user: User): String = s"${user.name} is ${user.age} yo"
  object PartialUserSerializer extends HTMLSerializer[User]:
    override def serialize(user: User): String = s"${user.name}"

  // :) type safety
  // :) multiple implementation for a type
  // :) can implement the trait for multiple types

  // exercise
  trait Equality[T]:
    def equals(v1:T, v2: T): Boolean 

  implicit object UserComparison extends Equality[User]:
    override def equals(v1: User, v2: User): Boolean = v1.name == v2.name

  /**
    * Type class + implicit
    */
  object HTMLSerializer:
    def serialize[T](value: T)(implicit serializer: HTMLSerializer[T]): String = 
      serializer.serialize(value)
    def apply[T](implicit serializer: HTMLSerializer[T]): HTMLSerializer[T] = serializer

  println(HTMLSerializer.serialize(user))
  println(HTMLSerializer[User].serialize(user)) // access to entire type class interface with apply()

  //exercise
  object Equality:
    def apply[T](a: T, b: T)(implicit eq: Equality[T]): Boolean = eq.equals(a,b)

  val user2 = User("John", 90)
  println(Equality(user, user2))
}
