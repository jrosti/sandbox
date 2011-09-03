
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
  
  def encode(number: String): Set[List[String]] = 
    if (number.isEmpty()) Set(List())
    else {
      for {
        split <- 1 to number.length
        word <- wordsForNum(number take split)
        rest <- encode(number drop split)
      } yield word :: rest
    }.toSet
    
  def translate(number: String): Set[String] = encode(number) map (_ mkString " ") 
}

object Phonecode extends App {
  val dict = io.Source.fromFile("/usr/share/dict/words")
     .getLines.filter(_.length > 1).filter(_.matches("[a-zA-Z]+")).toList  
  val coder = new Coder("Scala" :: "rocks" :: dict)
  println(coder.translate("7225276257"))
}

