package implicits

object Givens extends App:
  // implicit in Scala 3: given (<=>  implicit val)
  val sortedList = List(1,2,3).sorted
  object Givenv1:
    given descendingOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _)
  // given defined order is used for the list as long as in the same scope
  // while implicit vals defines after sorted is called will not affect the sorting 

  // use a annoynymous class with keyword with
  object Givenv2:
    given descendingOrderingv2: Ordering[Int] with {
      override def compare(x: Int, y: Int): Int = y-x
    }

  import Givenv2.given // imports all givens, or specify which one

  // implicit args <=> using
  def extremes[A](list: List[A])(using ordering: Ordering[A]): (A,A) = {
    val sortedList = list.sorted
    (sortedList.head, sortedList.last)
  }

  // implicit def -> synthesize new implicit values 
  trait Combinator[A]:
    def combine(x: A, y: A): A

  // with implicit
  implicit def listOrdering[A](implicit order: Ordering[A], comb: Combinator[A]): Ordering[List[A]] = 
    new Ordering[List[A]] {
      override def compare(x: List[A], y: List[A]): Int = 
        val sumx = x.reduce(comb.combine)
        val sumy = y.reduce(comb.combine)
        order.compare(sumx, sumy)
    }
  
  // Scala 3
  given listOrderingv2[A](using order: Ordering[A], comb: Combinator[A]): Ordering[List[A]] with {
    override def compare(x: List[A], y: List[A]): Int = 
        val sumx = x.reduce(comb.combine)
        val sumy = y.reduce(comb.combine)
        order.compare(sumx, sumy)
  }

  // Scala 3 discourages implicit conversion as it's abused in 2
  // need to explicitly import implicitConvers -> required in Scala 3
  case class Person(name: String):
    def greet(): String = s"hello $name"

  import scala.language.implicitConversions
  given StringToPersonConversion: Conversion[String, Person] with {
    override def apply(x: String): Person = Person(x)
  }