import scala.io.StdIn.readLine

/** The main function of the chess game.
  *
  * Creates a chess game and it's white turn to play. The game continue until
  * one player win or a stalemate occurs
  */
@main def playChess: Unit =

  val game: Game = Game()

  while (game.winner == "") {
    game.play()
  }

  println(game.winner + " won!")
  println(game.board.visualizeBoard(game.winner))
