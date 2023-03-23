package typesystem

object SelfTypes extends App {
  trait Instrumentalist:
    def play(): Unit
  
  trait Singer:
    self: Instrumentalist => // self type: enforces implementations of singer to implement instrumentalist as well 
    def sing(): Unit 

  class LeadSinger extends Singer with Instrumentalist:
    override def play(): Unit = ???
    override def sing(): Unit = ???

  // vs inheritance 
  class A 
  class B extends A 

  trait T 
  trait S:
    self: T => // S REQUIES T

  // cake pattern (dependency injection) -> layers of abstraction 
  trait ScalaComponent:
    def action(x: Int): String 

  trait DependentComponent:
    self: ScalaComponent =>
    def dependentAction(x: Int): String = action(x) // can use methods from ScalaComponent directly 

  trait ScalaApplication:
    self: DependentComponent with ScalaComponent =>

  // layer 1: small components 
  trait Picture extends ScalaComponent
  trait Stats extends ScalaComponent

  // layer 2: compose 
  trait Profile extends DependentComponent with Picture

  // layer3
  trait MyApp extends ScalaApplication with Profile

  // cyclical dependencies is not a problem with self typing 
  trait X:
    self: Y=>

  trait Y:
    self: X=>
}
