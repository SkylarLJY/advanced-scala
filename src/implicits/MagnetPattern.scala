package implicits

object MagnetPattern extends App:
  /**
    * Problem with method overloading 
    * - type erasure: eg. having Future[A] and Future[B] as parameter doesn't compile
    *   b/c the type parameters(A&B) are not known at compile time
    * - lifting doesn't work- code duplication
    * - type inference & default args
    */

    // instead: define a single method & use implicit conversion for the parameters 
    class P2PReq
    class P2PRes
    trait Magnet[Result]:
      def apply(): Result 

    def receive[R](magnet: Magnet[R]): R = magnet()

    implicit class FromReq(req: P2PReq) extends Magnet[Int]: // magnet of return type 
      def apply(): Int = {
        println("handling req")
        42
      }

    receive(new P2PReq)

    /* Pros: 
      - no type erasure problem 
      - lifting works
     */
    // overloading
    trait MathLib:
      def add1(x: Int): Int = x+1
      def add1(s: String): Int = s.toInt + 1
    // magnetized
    trait AddMagnet: // no type parameter cause the compiler doesn't know which type to lift for 
      def apply(): Int

    def add1(magnet: AddMagnet): Int = magnet()
    
    implicit class AddInt(x: Int) extends AddMagnet:
      def apply(): Int = x+1
    implicit class AddString(s: String) extends AddMagnet:
      def apply(): Int = s.toInt + 1

    val addFunc = add1 _
    println(addFunc(9))

    /**
      * Cons:
        - verbose
        - hard to read 
        - can' name/place default args 
        - call by name doesn't work correctly with side effect 
      */
    trait Handler:
      def handle(s: =>String): Unit = 
        println(s)
        println(s)
    
    trait HandleMagnet:
      def apply(): Unit 
    
    def handle(m: HandleMagnet): Unit = m()

    implicit class StringMagnet(s: =>String) extends HandleMagnet:
      def apply(): Unit = 
        println(s)
        println(s)

    def sideEffect(): String = 
      println("side effect")
      "result"

    handle(sideEffect())
    handle {
      println("side effect 2")
      "not in a method"
    }

    
        
    
    

