import scala.collection.immutable.BitSet

class Board(val crosses: BitSet = BitSet(), val noughts: BitSet = BitSet(), val alternated: Boolean = false, val boardSize: Int = 3) {

  val fullSet = (BitSet(1) /: (2 to boardSize * boardSize))(_ ^ BitSet(_))

  def emptySlots: BitSet = crosses ^ noughts ^ fullSet

  def addTic(tic: Int): Board =
    if ((crosses contains tic) || (noughts contains tic))
      throw new IllegalArgumentException("Position already taken")
    else
      new Board(noughts, crosses ^ BitSet(tic), !alternated)

  def isLeftBorder(pos: Int): Boolean = (pos - 1) % boardSize == 0
  def isRightBorder(pos: Int): Boolean = isLeftBorder(pos + 1)

  def isValidConnection(pos: Int, move: (Int => Int)): Boolean =
    !((isLeftBorder(pos) && isRightBorder(move(pos)))
      || (isRightBorder(pos) && isLeftBorder(move(pos))))

  def completeRowToDirection(pos: Int, direction: (Int => Int)): BitSet =
    if (isValidConnection(pos, direction) && crosses.contains(direction(pos)))
      BitSet(direction(pos)) ^ completeRowToDirection(direction(pos), direction)
    else
      BitSet()

  def rows(pos: Int): List[BitSet] = {
    if (!(crosses contains pos)) List()
    val horizontal = ((pos: Int) => pos + 1, (pos: Int) => pos - 1)
    val vertical = ((pos: Int) => pos + boardSize, (pos: Int) => pos - boardSize)
    val diagonalUp = ((pos: Int) => pos - boardSize + 1, (pos: Int) => pos + boardSize - 1)
    val diagonalDown = ((pos: Int) => pos - boardSize - 1, (pos: Int) => pos + boardSize + 1)
    for (direction <- List(horizontal, diagonalUp, diagonalDown, vertical))
      yield completeRowToDirection(pos, direction _1) ^ BitSet(pos) ^ completeRowToDirection(pos, direction _2)
  }

  def hasWinningRow: Boolean = !crosses.forall(rows(_).forall(_.size < boardSize))

  def score: Int = {
    val alternatedBoard = new Board(noughts, crosses, !alternated)
    if (alternatedBoard hasWinningRow) 
      if (alternated) 1 else -1
    else 0
  }
}

sealed trait Tree[A];
case class Leaf[A](a: A) extends Tree[A];
case class Branch[A](ts: Stream[Tree[A]]) extends Tree[A]

object Game {

  def gameTree(board: Board): Tree[Board] =
    if (board.emptySlots.size == 0 || board.score != 0)
      Leaf(board)
    else
      Branch(board.emptySlots.toStream.map((pos: Int) => gameTree(board addTic pos)))

  def maximize(tree: Tree[Board]): Int = {
    tree match {
      case Leaf(board) => board.score
      case Branch(subgames) => subgames.map((tree: Tree[Board]) => minimize(tree)).max
    }
  }

  def minimize(tree: Tree[Board]): Int = {
    tree match {
      case Leaf(board) => board.score
      case Branch(subgames) => subgames.map((tree: Tree[Board]) => maximize(tree)).min
    }
  }

}

object TicTacToe extends App {
  import Game._
  val start = System.currentTimeMillis
  val gtree = gameTree(new Board)
  val gtreeCreated = System.currentTimeMillis

  if (maximize( gtree ) == 0) println ("Game is a draw.")
  val stop = System.currentTimeMillis
  
  val sec = (t:Long) => ( (t - start) / 1000.0)
  println("Time for gametree creation: " + sec(gtreeCreated) + "s and for finding the solution " + sec(stop) + "s.")
}