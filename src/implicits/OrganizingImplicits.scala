package implicits

import java.io.ObjectOutputStream.PutField

object OrganizingImplicits extends App{
  /**
    * Implicits on
    * - val, var
    * - objects
    * - accessor methods: defs with no parenthese 
    */

    case class Person(name: String, age: Int)

    val people = List(
      Person("Steve", 20),
      Person("Jeff", 4),
      Person("Tim", 89)
    )



    /**
      * Implicit scope
      * - normal scope: local scope 
      * - imported scope
      * - companions of all types involved in method signature 
      * eg. def sorted[B>:A](implicit ord: Ordering[B]): LIst[B]
      *   List -> Ordering -> A & subtypes 
      * 
      * Best practices:
        - Define the implicit in the campanion if can edit
        - if there are multiple possible values for it: defien the best one in the companion and other ones in different objects 
          So that users can import from these imlicits 
      */

      object NameOrdering:
        implicit val nameOrdering: Ordering[Person] = Ordering.fromLessThan((p1, p2) => p1.name < p2.name)
      object AgeOrdering:
        implicit val ageOrdering: Ordering[Person] = Ordering.fromLessThan((p1, p2) => p1.age<p2.age)
      
      import NameOrdering._ 
      println(people.sorted)

      // Exercise 
      case class Purchase(nUnits: Int, unitPrice: Double):
        def totalPrice: Double = nUnits * unitPrice
      object Purchase:
        implicit val totalPriceOrdering: Ordering[Purchase] = Ordering.fromLessThan(_.totalPrice < _.totalPrice)
      object UnitCountOrdering:
        implicit val unitCountOrdering: Ordering[Purchase] = Ordering.fromLessThan(_.nUnits<_.nUnits)

      val pList = List(
        Purchase(2, 50),
        Purchase(100, 0.5),
        Purchase(8, 80)
      )

      import UnitCountOrdering._
      println(pList.sorted)
      
}
