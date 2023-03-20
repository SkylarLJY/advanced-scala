package typesystem

object Variance extends App{
  trait Animal
  class Cat extends Animal 
  class Dog extends Animal 

  // +T indicates that T is a covariant type
  // param of type val is at covariant position
  // while type var at contravariant position 
  // problem of covariant type at contravariant positon: init as cat food then change the animal to dog
  class CovFood[+T](val animal: T) 
  val covFood: CovFood[Animal] = new CovFood[Cat](new Cat)

  class InvFood[T](var animal: T)
  val invFood: CovFood[Cat] = new CovFood[Cat](new Cat)
  
  class ContraFood[-T]
  val contraFood: ContraFood[Cat] = new ContraFood[Animal]
  /**
  trait CovCage[+T] {
    def addAnimal(animal: T) -> method parameters are at contravariant positions
  }
  else it would have the problems of declaring an animal cage with cat cage & add dog to it
  */

  // widening the type for convariant types 
  class MyList[+A] {
    def add[B >: A](element: B): MyList[B] = new MyList[B]
  }

  // method returns are at covariant position 
  // else if declare a cat shop with a new animal shop, one can get a dog from the shop
  class PetShop[-T] {
    def get[S <: T](default: S): S = default
  }

  /**
    * Args are at covariant positions
    * Returns are at contravariant position 
    */
}
