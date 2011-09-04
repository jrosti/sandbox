
/** 
 * reaktor dev day, M. Odersky
 */
class Coder(words: List[String]) {

  private val mnemonics = Map(
      '2' -> "ABC", '3' -> "DEF", '4' -> "GHI", '5' -> "JKL", 
      '6' -> "MNO", '7' -> "PQRS", '8' -> "TUV", '9' -> "WXYZ")
      
  private val charCode: Map[Char, Char] =
    for ((digit, str) <- mnemonics; ltr <- str) yield (ltr -> digit)
          
  private def wordCode(word: String): String = word.toUpperCase map charCode
    
  private val wordsForNum: Map[String, Seq[String]] = (words groupBy wordCode) withDefaultValue List()
  
  def encode(number: String): List[List[String]] = 
    if (number.isEmpty()) List(List())
    else {
      for {
        split <- 1 to number.length
        word <- wordsForNum(number take split)
        rest <- encode(number drop split)
      } yield word :: rest
    }.toList
    
}

object Phonecode extends App {
  val start = System.currentTimeMillis()
  val dict = io.Source.fromFile("../haskell/testwords")
     .getLines.filter(_.length > 1).filter(_.matches("[a-zA-Z]+")).toList  
  val coder = new Coder(dict)
  println("Using " + dict.length + " words.")
  println("Found " + coder.encode("222667542489").length + " solutions.")
  val stop = System.currentTimeMillis()
  stop - start
  println("Time " + ((stop - start)/1000.0) + "s")
}

