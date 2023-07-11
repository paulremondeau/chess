class Board:

  private var finished: Boolean = false

  private val _board: Array[Array[Piece]] =
    (0 to 7)
      .map(x => (0 to 7).map(y => Piece("_", Position(x, y))).toArray)
      .toArray

  def initialize(): Unit =
    _board(6) = (0 to 7).map(y => Pawn("B", Position(6, y))).toArray
    _board(1) = (0 to 7).map(y => Pawn("W", Position(1, y))).toArray

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

  def board: Array[Array[Piece]] = _board

  override def toString(): String =
    visualizeBoard("W")

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

  def getAvailableMovements(piece: Piece): List[Position] =
    val pieceColor: String = piece.color
    val enemiesColor = if pieceColor == "W" then "B" else "W"
    val friendlyPieces: List[Piece] =
      board
        .map(x => x.filter(y => y.color == pieceColor))
        .fold(Array[Piece]())((x, y) => x ++ y)
        .toList
    val enemiesPieces: List[Piece] =
      board
        .map(x => x.filter(y => y.color == enemiesColor))
        .fold(Array[Piece]())((x, y) => x ++ y)
        .toList

    val availableMovements: List[Position] =
      piece.availableMovements(friendlyPieces, enemiesPieces)

    availableMovements

  def movePiece(piece: Piece, newPos: Position): Unit =

    _board(piece.position.row)(piece.position.column) =
      Piece("_", Position(piece.position.row, piece.position.column))

    piece.move(newPos)
    _board(newPos.row)(newPos.column) = piece

  def play(player: String): Unit =
    None

end Board
