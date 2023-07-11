import scala.io.StdIn.readLine
@main def hello: Unit =
  val game: Game = Game()

  while (game.winner == "") {
    game.play()
  }

  println(game.winner + " won!")
  println(game.board)

def msg = "I was compiled by Scala 3. :)"

def convertPos(input: String): (Int, Int) =

  val row: Char = input(1)
  val column: Char = input(0)

  val numericRow: Int = row.asDigit - 1
  val numericColumn: Int = ('a' to 'z').indexOf(column)

  // if (numericRaw.min(numericColumn) < 0) || (numericRaw.max(numericColumn) > 7)
  // then Position(-1, -1)
  // else Position(numericRaw, numericColumn)

  (numericRow, numericColumn)
