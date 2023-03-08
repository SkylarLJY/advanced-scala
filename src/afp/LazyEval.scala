package afp

import scala.compiletime.ops.boolean
import scala.language.postfixOps

object LazyEval extends App:
  // value evaluated when used for the first time, cached afterwards 
  // eg. side effects may not get excuted with lazy eval
  def sideEffectCond: Boolean = 
    println("side effect!")
    true

  lazy val lazyCond = sideEffectCond
  def simpleCond = false 
  println(if simpleCond && lazyCond then "yes" else "no") // lazy cond never evaluated so no side effect 

  // eg. in conjuction with call by name 
  /* call by need */
  def byName(n: Int) = 
    lazy val t = n // only evaluate once so only sleep 1s
    t+t+t
  def magicVal: Int = 
    Thread.sleep(1000)
    42
  println(byName(magicVal))

  // eg. filtering: withFilter uses lazy eval
  def lessThan30(i: Int): Boolean = 
    println(s"is $i less than 30?")
    i<30

  val lt30 = List(1,2,50,90).withFilter(lessThan30) 
  
  // for comprehension + withFilter + guard
  for 
    a<-List(1,2,3) 
    if a%2==0 // uses lazy eval
  yield a+1
  // same as 
  List(1,2,3).withFilter(_%2==0).map(_+1)
end LazyEval

