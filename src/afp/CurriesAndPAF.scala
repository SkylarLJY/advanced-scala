package afp

import scala.compiletime.ops.string

object CurriesAndPAF extends App{
  /* curries functions: multiple param list */
  val supperAdder : Int => Int => Int =  {
    x => y => x+y
  }

  val add3 = supperAdder(3)
  add3(5) // = 8
  supperAdder(3)(5) // same as above 

  def curriedAdder(x: Int)(y: Int): Int = x+y // curried method
  // method to function
  val add4: Int=>Int = curriedAdder(4) // need to specify returned type here

  // lifting: transforming method to function, also call "ETA-expansion"
  def inc(x: Int) = x+1
  List(1,2,3).map(inc) // here ETA-expansion happens: the compiler convers the method into a function 

  // partial function applications 
  val add5 = curriedAdder(5) _ // _ tells the compiler to do an EAT expansion 

  /* exercise */
  val addFunc = (x: Int, y: Int) => x+y
  def addMethod(x: Int, y: Int) = x+y
  def curriedAdd(x: Int)(y: Int) = x+y

  val add71 = n => addFunc(7, n)
  val add74 = addFunc.curried(7)
  val add72 = n => addMethod(7, n)
  val add73 = curriedAdd(7) _ 
  // alternative syntax: curriedAdd(7)(_)
  // turning methods into functions
  val add75 = addMethod(7, _: Int) 
  val add76 = addFunc(7, _: Int)
  

  // _
  def concatnator(a:String, b: String, c: String) = a+b+c
  val greet = concatnator("Hello I'm ", _: String, ". What's up")
  println(greet("Skylar"))

  /* exercise */
  val formater: String => Double => String = format => num => format.format(num)
  val format42f = formater("%4.2f")
  val format86f = formater("%8.6f") 
  println(format42f(Math.PI))
  println(format86f(Math.E))
  List(1.0,2.6,3.3).map(format42f).foreach(println)

  // call by name vs call by function
  // int, method, parenthesis method, lambda, PAF
  def byName(n: => Int) = n+1
  def byFunc(f: ()=>Int) = f()+1
  def method: Int = 42
  def parenMethod(): Int = 42

  println(byName(0))
  println(byFunc(()=>1))
  byName(method)
  byName(parenMethod())
  byName((()=>123)())
  // byName(parenMethod) doesn't work for scala 3
  // parameterless methods can't be used in byFunc
  byFunc(parenMethod) // ETA expansion 
}
