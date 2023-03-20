package typesystem.exercise

object Parking extends App{
  /**
    * 1. Invariant, covariant, contravariant parking lot with List[+T]
    * 2. with invariant list
    * 3. make it monad
    */
    class Vehicle
    class Bike extends Vehicle
    class Car extends Vehicle

    class Parking[T](things: List[T]):
      def park(vehicle: T): Parking[T] = ???
      def impound(vehicles: List[T]): Parking[T] = ???
      def check(condition: String): List[T] = ???
      def flatMap[S](f: T=>Parking[S]): Parking[S] = ???

    class CovParking[+A](things: List[A]):
      def park[B>:A](vehicle: B): CovParking[B] = ???
      def impound[B>:A](vehicles: List[B]): CovParking[B] = ???
      def check(conditions: String): List[A] = ???
      def flatMap[B](f: A=>CovParking[B]): CovParking[B] = ???

    class ContraParking[-A](things: List[A]):
      def park(vehicle: A): ContraParking[A] = ???
      def impound(vehicles: List[A]): ContraParking[A] = ???
      def check[B<:A](conditions: String): List[B]= ???
      // Function1[A, ContraParking[B]]
      // A is at a double covariant position -> becomes contravariant 
      def flatMap[B<:A, T](f: B=>ContraParking[T]): ContraParking[T] = ???

    class IList[T]
    class CovParkingIList[+A](things: List[A]):
      def impound[B>:A](vehicles: IList[B]): CovParkingIList[B] = ???
      def check[B>:A](conditions: String): List[B] = ???

    /**
      * - Use covariance for collections of things
      * - use contravariance for collection of actions 
      */
}
