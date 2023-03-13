package implicits

object ImplicitsIntro extends App{
  case class Person(name: String):
    def greet = s"Hi $name"

  implicit def fromStringToPerson(str: String): Person = Person(str)

  println("Peter".greet)

  // implicit parameter vs default parameters: implicit params found by compiler 
  def increment(x: Int)(implicit amount: Int) = x+amount
  implicit def defaultInt: Int = 2
  increment(8)
}
