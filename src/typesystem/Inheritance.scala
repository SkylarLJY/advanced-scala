package typesystem


class Inheritance extends App: 
  // convenience 
  trait Writer[T]:
    def write(value: T): Unit
  trait Closeable:
    def close(status: Int): Unit 
  trait GenericStream[T]:
    def foreach(f: T=>Unit): Unit 

  def processStream[T](stream: GenericStream[T] with Writer[T] with Closeable): Unit = 
    stream.foreach(println)
    stream.close(200)

  // the diamond problem
  trait  Aminal:
    def name: String
  trait Lion extends Aminal:
    override def name: String = "lion"
  trait Tiger extends Aminal:
    override def name: String = "tiger"

  class Mutant extends Lion with Tiger

  val m = new Mutant
  m.name // "tiger" as the override of Tiger comes last  

  // type linearization with super
  trait Cold:
    def print: Unit = println("cold")
  trait Green extends Cold:
    override def print: Unit =
      println("green")
      super.print
  trait Blue extends Cold:
    override def print: Unit = 
      println("blue")
      super.print
  class Red:
    def print: Unit = println("red")

  class White extends Red with Blue with Green:
    override def print: Unit =
      println("white")
      super.print

  val w = new White
  w.print
  /**
    * AnyRef with <Red>
    *   with (Cold with <Blue>) -> (AnyRef with <Cold? with <Blue>)
    *   with ...
    * 
    * gets linearized to 
    *   AnyRef with <Red> with <Cold> with <Blue> with <Green>
    * (Everything only appear in hierarchy once)
    */