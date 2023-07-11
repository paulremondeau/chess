class Pawn(c: String, p: Position) extends Piece(c, p):

  private var enPassant: Boolean = false

  override def toString(): String =
    if color == "B" then "♙"
    else "♟︎"

  override def availableMovements(
      friendlyPieces: List[Piece],
      enemiesPieces: List[Piece]
  ): List[Position] =
    val movement: Int =
      if (color == "W") then 1 else -1

    val (row: Int, column: Int) = (position.row, position.column)
    val moveColumn: List[Position] =
      if (friendlyPieces ++ enemiesPieces)
          .filter(piece =>
            piece.position.column == column && piece.position.row == row + movement
          )
          .toList == List()
      then List(Position(row + movement, column))
      else List()

    val firstMove: List[Position] =
      if ((color == "W" && row == 1) || (color == "B" && row == 6))
        && (friendlyPieces ++ enemiesPieces)
          .filter(piece =>
            piece.position.column == column && (piece.position.row == row + movement
              || piece.position.row == row + 2 * movement)
          )
          .toList == List()
      then List(Position(row + 2 * movement, column))
      else List()

    val capturePiece: List[Position] =
      enemiesPieces
        .filter(piece =>
          piece.position.row == row + movement && (piece.position.column == column + 1 || piece.position.column == column - 1)
        )
        .map(x => x.position)

    (moveColumn ++ capturePiece ++ firstMove).filter(pos =>
      (pos.row <= 7) && (pos.row >= 0)
    )

  def promote(kind: String): Piece =
    kind match
      case "Q" => Queen(color, position)
      case "B" => Bishop(color, position)
      case "R" => Rook(color, position)
      case "N" => Knight(color, position)
      case _   => Piece(color, position)

end Pawn
