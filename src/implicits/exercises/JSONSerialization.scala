package implicits.exercises


import java.util.Date

object JSONSerialization extends App:
  case class User(name: String, age: Int)
  case class Post(content: String, time: Date)
  case class Feed(user: User, posts: List[Post])

  /**
    * 1. itermediate data type for int, string, date and list
    * 2. type class to support conversion from different classes to the intermediate type
    * 3. serialize 
    */

  sealed trait JSONValue:
    // def construct(value: T): JSONValue[T]
    def stringify: String

  final case class JSONString(string: String) extends JSONValue:
    // def construct(value: String): JSONValue[String] = s"\"$string\""
    def stringify: String = string
    

  final case class JSONNumber(n: Int) extends JSONValue:
    def stringify: String = n.toString()

  final case class JSONList(l: List[JSONValue]) extends JSONValue:
    def stringify: String = l.map(_.stringify).mkString("[", ",", "]")

  final case class JSONDate(date: Date) extends JSONValue:
    def stringify: String = date.toString()

  final case class JSONObject(objects: Map[String, JSONValue]) extends JSONValue:
    def stringify: String = objects.map {
      case (k, v) => "\""+ k + "\": " + v.stringify
    }.mkString("{", ",", "}")

  trait JSONConverter[T]:
    def convert(value: T): JSONValue

  implicit object NumberConverter extends JSONConverter[Int]:
    def convert(value: Int): JSONValue = JSONNumber(value)

  implicit object StringConverter extends JSONConverter[String]:
    def convert(value: String): JSONValue = JSONString(value)

  implicit object DateConverter extends JSONConverter[Date]:
    def convert(value: Date): JSONValue = JSONDate(value)
  
  implicit object ListConverter extends JSONConverter[List[JSONObject]]:
    def convert(value: List[JSONObject]): JSONValue = JSONList(value)

  implicit object UserConverter extends JSONConverter[User]:
    def convert(value: User): JSONValue = JSONObject(Map(
      "name"-> JSONString(value.name),
      "age"-> JSONNumber(value.age)
    ))

  implicit object PostConverter extends JSONConverter[Post]:
    def convert(value: Post): JSONValue = JSONObject(Map(
      "content" -> JSONString(value.content),
      "timePosted" -> JSONDate(value.time)
    ))

  implicit object FeedConverter extends JSONConverter[Feed]:
    def convert(value: Feed): JSONValue = JSONObject(Map(
      "user" -> UserConverter.convert(value.user), // dependency on other other converters :(
      "feeds" -> JSONList(value.posts.map(PostConverter.convert))
    ))

  implicit class JSONOps[T](value: T):
    def toJson(implicit converter: JSONConverter[T]): JSONValue = converter.convert(value)
      

  val now = new Date(System.currentTimeMillis())
  val user = User("Kyle", 25)
  val feeds = Feed(user, List(
    Post("hello", now),
    Post("world", now )
  ))

  println(feeds.toJson.stringify)

  