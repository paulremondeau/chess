/** The board game.
  *
  * The board contains a 8x8 matrix with every square containing a piece, either
  * black, white or neutral.
  */
class Board:

  /** The 8x8 matrix containing the piece. When creatad, all squares contain
    * neutral pieces.
    */
  private val _board: Array[Array[Piece]] =
    (0 to 7)
      .map(x => (0 to 7).map(y => Piece("_", Position(x, y))).toArray)
      .toArray

  /** The board matrix.
    *
    * @return
    *   The board matrix.
    */
  def board: Array[Array[Piece]] = _board

  /** Initialize the chess board.
    *
    * Put all the pieces on their starting square. White pieces on the 1st row,
    * white pawns on the 2nd row, black pieces on the 8th row and black pawns on
    * the 7th row.
    */
  def initialize(): Unit =
    _board(6) = (0 to 7).map(y => Pawn("B", Position(6, y))).toArray
    _board(1) = (0 to 7).map(y => Pawn("W", Position(1, y))).toArray

    // Put all the pieces of a given color on the given row
    // in the right order.
    val fillPieceRow = (color: String, row: Int) =>
      Array[Piece](
        Rook(color, Position(row, 0)),
        Knight(color, Position(row, 1)),
        Bishop(color, Position(row, 2)),
        Queen(color, Position(row, 3)),
        King(color, Position(row, 4)),
        Bishop(color, Position(row, 5)),
        Knight(color, Position(row, 6)),
        Rook(color, Position(row, 7))
      )

    _board(0) = fillPieceRow.apply("W", 0)
    _board(7) = fillPieceRow.apply("B", 7)

  override def toString(): String =
    visualizeBoard("W")

  /** Visualize the board.
    *
    * Shows the chess board from either white or black perspective. Columns
    * names are indicates at the bottom, and row numbers are on the left side of
    * each row.
    *
    * Every piece is represented as their toString() method.
    *
    * @param side
    *   The perspective of the board, either black or white.
    * @return
    *   The chess board with all the pieces.
    */
  def visualizeBoard(side: String): String =

    val lineSep: String = "  ---------------------------------\n"
    val rowsStep: Array[String] = _board.map(x =>
      (if side == "B" then x.reverse
       else x).mkString(" | ") + " |\n"
    )
    val rows: Array[String] =
      ((1 to 8) zip rowsStep).map((x, y) => x.toString() + " | " + y).toArray
    val output: String = (List.fill(10)(lineSep) zip (if side == "B" then rows
                                                      else rows.reverse))
      .map((x, y) => x + y)
      .mkString("")

    val bottom: String =
      "    " + (if side == "B" then ('a' to 'h').toList.reverse
                else ('a' to 'h')).mkString("   ")

    "\n" + output + lineSep + bottom

  /** Move the piece on the board.
    *
    * It first replaces its starting square with a neutral piece. It then
    * replaces the target position with the piece.
    *
    * @param piece
    *   The piece to move.
    * @param newPos
    *   The position the piece moves.
    */
  def movePiece(piece: Piece, newPos: Position): Unit =

    _board(piece.position.row)(piece.position.column) =
      Piece("_", Position(piece.position.row, piece.position.column))

    piece.move(newPos)
    _board(newPos.row)(newPos.column) = piece

end Board
