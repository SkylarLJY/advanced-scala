package typesystem

object HigherKindedTypes {
  trait Monad[F[_], A]:
    def map[B](f: A=>B): F[B]
    def flatMap[B](f:A=>F[B]): F[B]

  class MonadList[A](list: List[A]) extends Monad[List, A]:
    override def flatMap[B](f: A => List[B]): List[B] = list.flatMap(f)
    override def map[B](f: A => B): List[B] = list.map(f)

  given MonadConversion[A]: Conversion[List[A], MonadList[A]] with {
    override def apply(x: List[A]): MonadList[A] = new MonadList[A](x)
  }

//  given MonadList[A]: Monad[List, A] with {
//    override def flatMap[B](f: A => List[B]): List[B] = ???
//
//    override def map[B](f: A => B): List[B] = ???
//
//    def apply(l: List[A]): MonadList[A] = ???
//  }

//  given MonadOp[F[_], A]: Monad[F, A] with {
//    override def flatMap[B](f: A => F[B]): F[B] = ???
//
//    override def map[B](f: A => B): F[B] = ???
//    def apply(m: F[A]): Monad[F, A] = ???
//  }
  val ml = MonadList(List(1,2,3))

  // applies to any monad, no need to define for each class 
  def multiply[F[_], A, B](ma: Monad[F,A], mb: Monad[F, B]): F[(A, B)] =
    for 
      a<-ma
      b<-mb
    yield (a, b)

  // multiply(MonadList(List(1,2,3)),MonadList(List("a", "b", "c")))
  multiply(List(1,2,3), List("a", "b", "c"))
}
