import scala.util.Try

object DarkSyntaxSugar extends App{
  // 1 - calling single arg methods with {statement}
  def funcSingleArg(arg: Int): String = ???
  val callSingleArd = funcSingleArg {
    // some operations
    42
  }
  // eg
  val tryInstance = Try {
    throw new RuntimeException
  }

  // 2 - single abstract method
  trait Action{
    def act(x: Int): Int
  }
  // instead of new Action & override, use lambda
  val actionInstane: Action = (x: Int) => x+1

  // 3 - :: & #::
  val prependList = 2 :: List()
  // operators ending in a ":" are right associatve -> evaluated from right to left
  class MyStream[T] {
    def -->:(value: T): MyStream[T] = this
  }
  val myStream = 1-->:2-->:new MyStream[Int]

  // 4 - multi word method naming 
  class TeenGirl(name: String){
    def `and then said`(gossip: String): Unit = println(s"$name said $gossip")
  }

  val lilly = new TeenGirl("Lilly")
  lilly `and then said` "something"

  // 5 - infix types 
  class Composite[A, B]
  val comp: Int Composite String = ???

  class -->[A, B]
  val towards: Int --> String = ???

  // 6 - update()
  val arr = Array(1,2,3)
  arr(2) = 7
  // expands to 
  arr.update(2, 7)
  // used in mutable collections (& apply())

  // 7 - setters for mutable containers 
  class Mutable{
    private var internalMember: Int = 0
    def member: Int = internalMember // getter
    def member_=(value: Int): Unit = internalMember=value
  }

  val mutable = new Mutable
  mutable.member = 42


}
