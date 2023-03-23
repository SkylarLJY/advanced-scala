package typesystem

object TypeMember {
  class Animal 
  class Cat extends Animal 
  class Dog extends Animal 

  class AnimalCollection:
    type AnimalType // abstract type member 
    type BoundedType <: Animal 
    type SuperBoundedType >: Dog <: Animal 
    type AnimalC = Cat 

  val ac = new AnimalCollection
  val dog: ac.AnimalType = ???
  val pup: ac.SuperBoundedType = new Dog

  type CatAlias = Cat 

  // can use type instead of generics
  trait MyList:
    type T 
    def add(element:T): MyList

  class NonEmptyList(value: Int) extends MyList:
    override type T = Int 
    override def add(element: Int): MyList = ???

  // .type 
  val cat = new Cat
  type CatType = cat.type // but can't new on it 

  /**
    * exercise: enforce a type to be applicable to some types only 
    */
    // can't modify the MList trait
  trait MList:
    type A
    def head: A
    def tail: MList 

  trait ApplicableToNumbers:
    type A <: Number

   // should only allow Number to be list items
   // a implementation with A=String should not compile 

  class IntList(hd: Integer, tl: IntList) extends MList with ApplicableToNumbers:
    type A = Integer
    def head = hd
    def tail = tl
}
