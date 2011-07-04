import scala.annotation.tailrec

object Factorials {

	def fac0(n : Long) : Long = {
		var result = 1
		var i = 1
		while (i <= n) {
			result = result * i
			i += 1
		}
		return result
	}

	def fac01(n: Long) :Long = {
		var result:Long = 1
		for (i <- (1 to n)) {
			result *= i
		}
		result
	}

	def fac1(n :Long) :Long = {
		var result: Long = 1
		(1 to n).foreach(i => result *= i)
		result
	} 

	def fac2(n :Long) :Long = if (n == 0) 1 else n * fac2(n-1)
	
	def fac3(n :Long) = if (n==0) 1 else (1 to n) reduceLeft (_*_)

	def fac4(n :Long) = ((1) /: (1 to n)) (_*_)

	class RichStream[A](str: =>Stream[A]) {
		def ::(hd: A) = Stream.cons(hd, str)
	}
	implicit def streamToRichStream[A](str: => Stream[A]) = new RichStream(str)	
	def from(n: Long): Stream[Long] = n :: from(n + 1)
	val nats = from(1)
	def fac5(n: Long) : Long = (nats take n foldLeft) (1) (_*_)
	
	@tailrec def fac6acc(a :Long, n :Long) :Long = n match {
	  case 0 => a
	  case _ => fac6acc(n*a, n-1) 
	}
	val fac6 = fac6acc(1, _:Long)
	
	@tailrec def fac7cps(k :Long=>Long, n :Long) :Long = n match {
	  case 0 => k(1)
	  case _ => fac7cps(k compose (n*_), n-1) 
	}
	val fac7 = fac7cps(x=>x, _:Long)
	
	def fac8(n: Long) = (1 to n) product
	
	type Impls = List[Pair[Long=>Long, String]]	
	val impls : Impls = List(  Pair(fac0, "Iterative while")
						, Pair(fac01, "Iterative for-loop through a sequence")
						, Pair(fac1, "Iterative foreach sequence with a side effect lambda")
						, Pair(fac2, "Classic recursive")
						, Pair(fac3, "Reducing from left")
						, Pair(fac4, "Folding from left")
						, Pair(fac5, "Infinite secuence generator")
						, Pair(fac6, "Accumulated parameter recursion")
						, Pair(fac7, "Continuation passing style")
						, Pair(fac8, "Product operation to a sequence.")
						)

	val computeDefault = compute(29, 10000, _ :Long=>Long, _ :String) 

	def compute(n: Long, iterations: Long, funct :Long=>Long, name : String) : Long = {
		println("Implementation: " + name)
		println("Computing " + n + "! = " + funct(n) + " repeat " + iterations + " times.")
		var i :Long = 0 
		val start = System.currentTimeMillis()
		while (i < iterations) {
			funct(n)
			i += 1
		}
		val stop = System.currentTimeMillis()
		stop - start
	}
}


object Main {
	
  def main(args : Array[String]) : Unit = {
	 import Factorials._
	 val run =  (_:Unit) => {for (impl <- impls) {
	   val millis = computeDefault(impl _1, impl _2)
	   println(millis + "ms\n")
	 }
	}
	run(1)
	run(2)
  }
}
