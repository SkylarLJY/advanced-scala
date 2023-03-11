package concurrency

// import for the implicit parameter of future 
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Random, Success}
import scala.concurrent.Await
import scala.concurrent.duration._

object FuturesAndPromises extends App:
  def calculateLifeMeaning: Int = 
    Thread.sleep(2000)
    42

  val aFuture = Future {
    calculateLifeMeaning // do this in another thread 
  }

  aFuture.value // -> Option[Try[Int]]

  aFuture.onComplete{ // returns Unit, used for side effects 
    case Failure(exception) => println("failed with" + exception)
    case Success(value) => println(value)
  }

  Thread.sleep(3000)

  // mini social network
  case class Profile(id: String, name: String):
    def poke(anotherProfile: Profile): Unit = 
      println(s"$name poking ${anotherProfile.name}")

  object TheNetwork:
    val nameDB = Map (
      "001" -> "user 1",
      "002" -> "user 2"
    )

    val bfMap = Map (
      "001" -> "002"
    )

    val rand = new Random()

    def fetchProfile(id: String): Future[Profile] = Future {
      Thread.sleep(rand.nextInt(300))
      Profile(id, nameDB(id))
    }

    def fetchBF(profile: Profile): Future[Profile] = Future {
      Thread.sleep(rand.nextInt(400))
      Profile(bfMap(profile.id), nameDB(bfMap(profile.id)))
    }
  end TheNetwork

  // naive approach to poke: call fetch methods in Future & act on complete -> :( nested onComplete

  // can use functional conpositions on Futures

  // poke with for-comprehension 
  for 
    user1 <- TheNetwork.fetchProfile("001")
    user2 <- TheNetwork.fetchBF(user1)
  do user1.poke(user2)

  Thread.sleep(1000)

  // fallbacks 
  // return a well defined profile 
  val recoverThrowable = TheNetwork.fetchProfile("unknown id").recover {
    case e: Throwable => Profile("dummy id", "Dummy")
  }
  // recoverWith with another Future
  val recoverFetch = TheNetwork.fetchProfile("unknow id").recoverWith {
    case e: Throwable => TheNetwork.fetchProfile("001")
  }

  val fallbackRes =TheNetwork.fetchProfile("unknow id").fallbackTo(TheNetwork.fetchProfile("001")) 

  /* Blocking on Futures */
  // banking app
  case class User(name: String)
  case class Transaction(sender: String, receiver: String, amount: Double, status: String)
  object BankingApp: 
    val name = "bank"
    def fetchUser(name: String): Future[User] = Future {
      Thread.sleep(300)
      User(name)
    }

    def createTransaction(user: User, merchant: String, amount: Double): Future[Transaction] = Future {
      Thread.sleep(200)
      Transaction(user.name, merchant, amount, "success")
    }

    def purchase(username: String, item: String, merchant: String, cost: Double): String = 
      val transStatusFuture = 
        for user <- fetchUser(username)
          trans <- createTransaction(user, merchant, cost)
        yield trans.status

      Await.result(transStatusFuture, 2.seconds)
  end BankingApp

  println(BankingApp.purchase("Jeff", "organic kale", "Whole Foods", 8.99))

  /* promises */
  val promise = Promise[Int]()
  val future = promise.future

  // producer consumer with promise 
  future.onComplete {
    case Success(res) => println("[consumer] got value: " + res)
  }

  val producer = new Thread(()=>{
    Thread.sleep(500)
    // fulfill the promise
    promise.success(42) // or promise.fail(ex)
    println()
  })
  producer.start()
  Thread.sleep(800)
  
end FuturesAndPromises
