package implicits

object ExtensionMethods extends App:
  // extension methods <=> implicit classes 
  case class Person(name: String):
    def greet: String = s"hello $name"

  extension (str: String){
    def greetAsPerson: String = Person(str).greet
  }

  extension (value: Int){
    def isEven: Boolean = value%2 == 0
  }

  4.isEven

  // generic extension 
  extension [A](list: List[A]) {
    def extremes(using ordering: Ordering[A]): (A, A) = (list.sorted.head, list.sorted.last)
  }
