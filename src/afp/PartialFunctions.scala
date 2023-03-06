package afp

object PartialFunctions extends App{
  // When a funciton only accepts partial domain as input 
  // {1,2,5} => Int
  val aPartialFunc: PartialFunction[Int, Int] = {
    case 1 => 11
    case 2 => 234
    case 5 => 333
  }
  // match error on invalid input 
  // PF utilities 
  aPartialFunc.isDefinedAt(8)
  // lift
  val lifted = aPartialFunc.lift // Int => Option[Int]
  //chaining 
  val pfChain = aPartialFunc.orElse[Int, Int] {
    case 6 => 999
  }

  // PF extend normal funcitons 
  val func: Int=>Int = {
    case 1=>99
  }

  // HOFs can accept PFs
  List(1,2,3).map{
    case 1 => 2
    case 2 => 3
    case 3 => 5
  }

  // PFs can only have 1 param type

  // exercise: construct PF on spot (anonymous class); dumb chatbot
  val bot = new PartialFunction[String, String] {
    override def apply(v1: String): String = v1 match
      case "Hello" => "hi"
    
    override def isDefinedAt(x: String): Boolean = x == "Hello"
    
  }
  scala.io.Source.stdin.getLines().foreach(l=>println(bot(l)))


}
