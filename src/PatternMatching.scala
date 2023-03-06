object PatternMatching extends App:
  val numbers = List(1)
  val description = numbers match
    case head :: Nil => head
    case _=>
  
  /* making a class compatible with pattern matching*/
  class Person(val name: String, val age: Int)
  // 1. create an object with unapply()
  // The matching is done against the object 
  object Person:
    def unapply(person: Person): Option[(String, Int)] = Some((person.name, person.age))
    // can over load unapply
    def unapply(age: Int): Option[String] = Some(if age<21 then "minor" else "major")

  val bob = new Person("Bob", 23)
  val greeting  = bob match
    case Person(n, a) => s"$n is $a"
  
  println(greeting)

  val legalStatus = bob.age match
    case Person(status) => s"status is $status"

  // exercise: math properties 
  val n: Int = 10
  val properties = n match
    case x if x<10 => "single digit"
    case x if x%2 == 0 => "even number"
    case _ => "no property"
  
  // create singleton objects for each property -> usually use lower case for singletons
  object even:
    def unapply(x: Int): Boolean = x%2 == 0

  object singleDigit: 
    def unapply(x: Int): Option[Boolean] = if x<10 then Some(true) else None

  val prop = n match
    case even => "is even"
    case singleDigit => "is single digit"
    case _ => "no property"

  println(prop)

  /* infix patterns */
  case class Or[A, B](a: A, b: B) // "Either" in scala 
  val either = Or(2, "two")
  val english = either match
    case number Or string => s"$number is $string in English"

  /*decomposing seqs for cusomized classes (_*)*/
  val varargs = numbers match
    case List(1, _*) => "starting with 1"

  abstract class MyList[+A]:
    def head: A = ???
    def tail: MyList[A] = ???

  case object Empty extends MyList[Nothing]
  case class Cons[+A](override val head: A, override val tail: MyList[A]) extends MyList[A]

  // define an unappluSeq[A]
  object MyList: 
    def unapplySeq[A](list: MyList[A]): Option[Seq[A]] =
      if list == Empty then Some(Seq.empty)
      else unapplySeq(list.tail).map(list.head +: _)

  val myList: MyList[Int] = Cons(1, Cons(2, Empty))
  val decompose = myList match
    case MyList(1, _*) => "starting with 1"
    case _ => "not starting with 1"

  println(decompose)

  /* custom return types */
  // no need to be an Option, as long as have isEmpty and get
  abstract class Wrapper[T]:
    def isEmpty: Boolean
    def get: T

  object PersonWrapper: 
    def unapply(person: Person): Wrapper[String] = new Wrapper[String] {
      def isEmpty: Boolean = false
      def get: String = person.name
    }
  
  println(bob match
    case PersonWrapper(n) => s"name is $n"
    case _ => "unknow person"
  )
  
end PatternMatching

