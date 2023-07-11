class Knight(c: String, p: Position) extends Piece(c, p):

  override def toString(): String =
    if color == "B" then "♘"
    else "♞"

  override def availableMovements(
      friendlyPieces: List[Piece],
      enemiesPieces: List[Piece]
  ): List[Position] =
    val row: Int = position.row
    val column: Int = position.column

    List[Position](
      Position(row + 1, column + 2),
      Position(row + 1, column - 2),
      Position(row - 1, column + 2),
      Position(row - 1, column - 2),
      Position(row + 2, column + 1),
      Position(row + 2, column - 1),
      Position(row - 2, column + 1),
      Position(row - 2, column - 1)
    ).filter(p => // Don't go outside the board !
      (p.row).min(p.column) >= 0 && (p.row).max(p.column) <= 7
    ).filter(p => !(friendlyPieces.map(_.position)).contains(p))

end Knight
