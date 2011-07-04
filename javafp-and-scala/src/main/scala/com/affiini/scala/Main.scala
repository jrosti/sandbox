package com.affiini.scala

object Main {
  def main(args : Array[String]) : Unit = { 
    var sum = 0
    (1 to 10).toStream.foreach(sum += _)
    println(sum)  
    println((1 to 10) reduceLeft (_+_))
    println(((1 to 10) /:) (0) (_+_))
  }
}
