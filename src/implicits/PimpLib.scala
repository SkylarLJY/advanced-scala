package implicits

object PimpLib extends App:
  // add functionalities through implicit classes: 1 param only
  // compiler doesn't do multiple layer implicit search 
  implicit class RichInt(val value: Int) extends AnyVal: // extends AnyVal for optimization purpose  
    def isEven: Boolean = value%2 == 0

    def times(f: ()=>Unit): Unit =  
      if value > 0 then 
        f()
        (value-1).times(f)
    def *[A](l: List[A]): List[A] =
      if value ==0 then List[A]()
      else l ++ (value-1)*l

  42.isEven

  // exercise 
  implicit class RichStr(str: String):
    def asInt: Int = Integer.valueOf(str)
    def encrypt(dist: Int): String = str.map(c=>(c+dist).asInstanceOf[Char])

  println("john".encrypt(2))
  3.times(()=>println("hello"))
  println(2*List(1,2,3))

  /**
    * Good practice:
      - keep type enrichment to implicit classes and type classes
      - avoid implicit def, it makes code hard to debug
      - package implicits clearly & only import when needed 
      - make type conversion specific 
    */
