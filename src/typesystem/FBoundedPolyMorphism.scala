package typesystem

object FBoundedPolyMorphism {
  trait Animal[A <: Animal[A]]: // recursive type: F-Bounded Polymorphism 
    def breed: List[Animal[A]]

  trait Cat extends Animal[Cat]:
    override def breed: List[Animal[Cat]] = ???

  // problem: if Cat extends Animal of a different type

  // sol: FBP + self type
  trait Animal2[A<:Animal2[A]]:
    self: A=>
    def breed: List[Animal2[A]]

  // problem: 1+ level of inheritance 
  trait Fish extends Animal2[Fish]
  class Shark extends Fish:
    override def breed: List[Animal2[Fish]] = List(new Salmon) // shouldn't be allowed 
  class Salmon extends Fish:
    override def breed: List[Animal2[Fish]] = ???

  // sol: type classes 
  trait Animal3[A]:
    def breed(animal: A): List[A]

  class Dog 
  object Dog:
    given Animal3[Dog] with {
      override def breed(animal: Dog): List[Dog] = List()
    }

  extension[A: Animal3] (a: A){
    def breed(using animal: Animal3[A]): List[A] = animal.breed(a)
  }

  val d = new Dog
  d.breed 
}
