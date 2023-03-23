package typesystem

object PathDependentTypes extends App{
  class Outer:
    class Inner
    type InnerType

  def aMethod: Int = 
    class HelperClass
    type HelperType
    3

  // the inner classes are per-instance 
  val outer = new Outer
  val inner = new outer.Inner

  val outer2 = new Outer
  // val inner2: outer.Inner = new outer2.Inner -> doesn't compile 

  // Outer#Inner is the super type of all Inner classes 

  /**
    * exercise: DB key with Int & String 
    * (for scala 2)
    */
  trait ItemLike:
    type Key
  trait Item[K] extends ItemLike:
    type Key = K
  trait IntItem extends Item[Int]
  trait StringItem extends Item[String]
  def get[ItemType<:ItemLike](key: ItemLike#Key): ItemType = ???
//  get[StringItem]("42") // ok
  // get[IntItem]("42") shouldn't compile

  

}
