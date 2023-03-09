package afp

object Monads  extends App{
    /* monad laws
    - left identity: unit(x).flatMap(f) = f(x)
    - right identity: attempt.flatMap(unit) = attempt 
    - associativity: attempt.flatMap(f).flatMap(g) == attempt.flatMap(x=>f(x).flatMap(g))
  */
  // Try monad
  trait Attempt[+A]:
    def flatMap[B](f: A=>Attempt[B]): Attempt[B]

  object Attempt:
    def apply[A](a: =>A): Attempt[A] = 
      try
        Success(a)
      catch 
        case e: Throwable => Fail(e)

  case class Success[+A](value: A) extends Attempt[A]:
    def flatMap[B](f: A => Attempt[B]): Attempt[B] = 
      try 
        f(value)
      catch 
        case e: Throwable => Fail(e)

  case class Fail(e: Throwable) extends Attempt[Nothing]:
    def flatMap[B](f: Nothing => Attempt[B]): Attempt[B] = this

    // exercise: lazy monad 
  object Lazy:
    def apply[A](a: =>A): Lazy[A] = new Lazy(a)

  class Lazy[+A](a: =>A) :
    // call by need 
    lazy val internal = a
    def use: A = internal
    // a is by name and applying f to it will evaluate it eagerly -> make f's input by name as well so that "this" if not evaluated again
    def flatMap[B](f: (=>A) => Lazy[B]): Lazy[B] = f(internal) 

  val lazyInstance = Lazy {
    println("lazy instance")
    42
  }
  // println(lazyInstance.use)
  val lazyFlatMap = lazyInstance.flatMap(x=>Lazy {x+1})
  // should only print once as the value is call by need now
  lazyInstance.use
  lazyFlatMap.use

    // exercise: Monad with map() & flatten() with flatMap()
  class Monad[A](value: A):
    val a = value
    def flatMap[B](f: A=> Monad[B]): Monad[B] = f(a)
    def Map[B](f: A=>B): Monad[B] = flatMap(x=>Monad(f(x)))
    def flatten(m: Monad[Monad[A]]): Monad[A] = m.flatMap(mx => mx)




}
