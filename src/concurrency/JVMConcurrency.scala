package concurrency

object JVMConcurrency:
  class BankAccount(var init: Int):
    var amount = init 
    def buy(price: Int): Unit = amount -= price 
    def buySafe(price: Int): Unit = synchronized {amount -= price}

  def bankRace(): Unit = 

    1 to 10000 foreach {_=>
      val bankAccount = new BankAccount(10)
      val t1 = new Thread(()=>bankAccount.buy(1))
      val t2 = new Thread(()=>bankAccount.buy(7))

      t1.start()
      t2.start()
      t1.join()
      t2.join()
      if bankAccount.amount != 2 then println("broke the account!")

    }

  def bankSafe(): Unit = 

    1 to 10000 foreach {_=>
      val bankAccount = new BankAccount(10)
      val t1 = new Thread(()=>bankAccount.buySafe(1))
      val t2 = new Thread(()=>bankAccount.buySafe(7))

      t1.start()
      t2.start()
      t1.join()
      t2.join()

      if bankAccount.amount != 2 then println("should be safe but broke the account!")
    }

  // exercise: nested thread reversed print
  def inceptionThread(i: Int, n: Int): Unit = 
    if i<n then
      val t = new Thread(()=>inceptionThread(i+1, n))
      t.start()
      t.join()
    println(s"hello from thread $i")

  // sleep fallacy: Thread.sleep() yields execution 
  // so if some other thread start to run & takes longer thant the sleep time 
  // it may mess up the execution order of sleeping thread
  // solution: join thread after main thread sleep 


  def main(args: Array[String]): Unit =
    // bankRace()
    // bankSafe()
    inceptionThread(0, 10)
end JVMConcurrency
