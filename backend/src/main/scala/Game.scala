import scala.io.StdIn.readLine

/** A game of chess.
  *
  * A game is played between two players, white and black.
  * @todo
  *   add clocks
  * @todo
  *   keep move orders somewhere
  * @todo
  *   draw rules (50 moves, 3 positions)
  */
class Game:

  /** The winner of the game
    *
    * While its the empty string, the game has no winner.
    */
  private var _winner: String = ""

  /** Access the winner of the game.
    *
    * If it's a stalemate, the winner is set to "_"
    *
    * @return
    *   The winner.
    */
  def winner: String = _winner

  /** Which player it is to play
    */
  private var turn: String = "W"

  /** The player of the next turn.
    */
  private var nextTurn: String = "B"

  /** @todo
    *   used to store the moves order.
    */
  private var moves: List[Any] = List() // Used to store the moves

  /** The chess board.
    */
  private val _board: Board = Board()

  /** Access the board of the game.
    *
    * @return
    *   The board.
    */
  def board: Board = _board
  _board.initialize()

  /** A list of all the pieces of the given color.
    */
  val colorPiece = (color: String) =>
    _board.board
      .map(x => x.filter(y => y.color == color))
      .fold(Array[Piece]())((x, y) => x ++ y)
      .toList

  /** Check if the input position is of the right format.
    *
    * This function checks if the given position is a correct chess square, i.e
    * of the form a1 -> h8
    *
    * @param input
    *   The given input.
    * @return
    *   true if the input is a correct position, false otherwise.
    */
  def validPosition(input: String): Boolean =
    if input.length() == 2 then
      if ('a' to 'h').contains(input(0)) then
        if ('1' to '8').contains(input(1)) then true
        else false
      else false
    else false

  /** Convert a string position to its int tuple equivalent.
    *
    * The given string is suppose to already be of good quality.
    *
    * @param input
    *   The string position to convert.
    * @return
    *   The square coordinate in matrix conventions.
    */
  def convertPos(input: String): (Int, Int) =

    val row: Char = input(1)
    val column: Char = input(0)

    val numericRow: Int = row.asDigit - 1
    val numericColumn: Int = ('a' to 'z').indexOf(column)

    (numericRow, numericColumn)

  /** Play a turn of chess.
    *
    * The function start by verifying if the game is not over (checkmate or
    * stalemate).
    *
    * If not, the player of which it's the turn pick a piece and then pick a
    * position to move this piece.
    */
  def play(): Unit =

    val friendlyPieces: List[Piece] = colorPiece(turn)
    val friendlyKing: King =
      friendlyPieces.filter(p => p.isInstanceOf[King]).head.asInstanceOf[King]

    val enemiesPieces: List[Piece] = colorPiece(nextTurn)
    val enemyKing: King =
      enemiesPieces.filter(p => p.isInstanceOf[King]).head.asInstanceOf[King]

    val availableMovements: Map[Piece, List[Position]] =
      friendlyPieces
        .map(x =>
          x -> x
            .availableMovements(friendlyPieces, enemiesPieces)
            .filter(y =>
              x.isValidMovement(
                y,
                friendlyPieces,
                enemiesPieces,
                friendlyKing
              )
            )
        )
        .toMap
    println(availableMovements)
    if availableMovements.map((x, y) => y.length).sum == 0
    then // No valid moves available
      if friendlyKing.isChecked(
          friendlyPieces,
          enemiesPieces
        ) // King is checked
      then _winner = nextTurn // Checkmate
      else _winner == "_" // Stalemate
    else // Game goes on
      println("It's " + turn + " turn to play.")
      println(_board.visualizeBoard(turn))

      var playerHasPlay: Boolean = false
      var selectedSquare = ""
      var (selectedRow: Int, selectedColumn: Int) = (0, 0)
      var validSquare = false

      while (!playerHasPlay) {
        selectedSquare = ""
        selectedRow = 0
        selectedColumn = 0
        validSquare = false

        while (!validSquare) {
          println("Choose a piece to move :")
          selectedSquare = readLine()
          if validPosition(selectedSquare) then
            selectedRow = convertPos(selectedSquare)(0)
            selectedColumn = convertPos(selectedSquare)(1)
            if (selectedRow
                .min(selectedColumn) >= 0) && (selectedRow.max(
                selectedColumn
              ) <= 7) && _board.board(selectedRow)(selectedColumn).color == turn
            then validSquare = true
            else validSquare = false
          else validSquare = false
        }

        var selectedPiece: Piece = _board.board(selectedRow)(selectedColumn)
        var movements = availableMovements.apply(selectedPiece)
        println("Select movements available : " + movements)

        if movements.length > 0 then

          var (selectedRowMove: Int, selectedColumnMove: Int) = (0, 0)
          val selectedPos: Position = Position(0, 0)
          validSquare = false

          while (!validSquare) {
            println(
              "Choose a square to move, or enter (x) to choose another piece :"
            )
            selectedSquare = readLine()
            if selectedSquare == "x" then validSquare = true
            else if validPosition(selectedSquare) then
              selectedRowMove = convertPos(selectedSquare)(0)
              selectedColumnMove = convertPos(selectedSquare)(1)
              selectedPos.move(selectedRowMove, selectedColumnMove)
              if movements.contains(selectedPos) then validSquare = true
              else validSquare = false
            else validSquare = false
          }

          if selectedSquare == "x" then playerHasPlay = false
          else

            val selectedPiece = _board.board(selectedRow)(selectedColumn)

            selectedPiece match
              case pawn: Pawn =>
                if (pawn.color == "B" && selectedRowMove == 0) || (pawn.color == "W" && selectedRowMove == 7) // pawn can promote
                then
                  println("Choose how to promote your pawn (Q, R, B, N): ")
                  var promotion = readLine()
                  while (
                    !List[String]("Q", "B", "R", "N").contains(promotion)
                  ) {
                    promotion = readLine()
                  }
                  val newPiece = pawn.promote(promotion)
                  _board.movePiece(
                    pawn,
                    selectedPos
                  ) // first move pawn...
                  _board.movePiece(
                    newPiece,
                    selectedPos
                  ) // ... then replace it by its promotion
                else if (pawn.position.row == selectedPos.row)
                then // en passant
                  val direction = if pawn.color == "W" then 1 else -1
                  _board.movePiece(
                    pawn,
                    Position(selectedPos.row + direction, selectedPos.column)
                  ) // move pawn en passant

                  _board.board(selectedPos.row)(selectedPos.column) =
                    Piece("_", selectedPos) // remove captured pawn
                else // move normally

                  if (selectedPos.row - pawn.position.row).abs > 1 then
                    pawn.enPassant_(true)

                  _board.movePiece(
                    pawn,
                    selectedPos
                  )

              case king: King =>
                if king.canCastle then
                  val castleableRooks: List[Piece] = friendlyPieces
                    .filter(_.isInstanceOf[Rook])
                    .filter(_.asInstanceOf[Rook].canCastle)
                  if castleableRooks
                      .map(_.position)
                      .contains(selectedPos)
                  then
                    // It's a castle
                    val castleRook: Rook =
                      castleableRooks
                        .filter(p => p.position == selectedPos)
                        .head
                        .asInstanceOf[Rook]
                    val direction: Int =
                      if selectedPos.column > king.position.column then 1
                      else -1

                    _board.movePiece(
                      castleRook,
                      Position(
                        king.position.row,
                        king.position.column + direction
                      )
                    )

                    _board.movePiece(
                      king,
                      Position(
                        king.position.row,
                        king.position.column + 2 * direction
                      )
                    )
                  else
                    _board.movePiece(
                      king,
                      selectedPos
                    )
                else
                  _board.movePiece(
                    king,
                    selectedPos
                  )
              case _ => // move normally
                _board.movePiece(
                  selectedPiece,
                  selectedPos
                )

            val temp: String = nextTurn
            nextTurn = turn
            turn = temp
            playerHasPlay = true
            enemiesPieces
              .filter(_.isInstanceOf[Pawn])
              .map(
                _.asInstanceOf[Pawn].enPassant_(false)
              ) // Disable en passant for all enemies pawns
        else
          println("No available movements.")
          playerHasPlay = false
      }

end Game
