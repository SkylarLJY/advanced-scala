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
    * part 2: Type class + implicit
    */
  object HTMLSerializer:
    def serialize[T](value: T)(implicit serializer: HTMLSerializer[T]): String = 
      serializer.serialize(value)
    def apply[T](implicit serializer: HTMLSerializer[T]): HTMLSerializer[T] = serializer

  println(HTMLSerializer.serialize(user))
  println(HTMLSerializer[User].serialize(user)) // access to entire type class interface with apply()


  /**
    * part 3
    */
  implicit class HTMLEnrichment[T](value: T):
    def toHTML(implicit serializer: HTMLSerializer[T]): String = serializer.serialize(value)

  user.toHTML // new HTMLEnrichment(user).toHTML(UserSerializer)
  // in this way the enrichment extends to new types & can choose implementation as well

  // context bounds: telling the compiler to inject implicit parameters 
  def htmlBoilerplate[T](content: T)(implicit serializer: HTMLSerializer[T]): String = 
    s"<div>${content.toHTML(serializer)}</div>"
    
  def htmlSugar[T : HTMLSerializer](content: T): String = 
    val serializer = implicitly[HTMLSerializer[T]]
    s"<div>${content.toHTML(serializer)}</div>"

  // implicitly -> surface the implicit values 
  case class Permissions(mask: String)

  implicit val defaultPermission: Permissions = Permissions("0744")
  // in other parts of the code 
  val perm = implicitly[Permissions]
  // can use implicitly in context bounding to access the implicit param

}
