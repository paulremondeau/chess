class Bishop(c: String, p: Position) extends Piece(c, p):

  override def toString(): String =
    if color == "B" then "♗"
    else "♝"

  override def availableMovements(
      friendlyPieces: List[Piece],
      enemiesPieces: List[Piece]
  ): List[Position] =
    (longRangeMovements( // first diagonal upward
      friendlyPieces,
      enemiesPieces,
      1,
      1
    ) ++ longRangeMovements( // first diagonal downward
      friendlyPieces,
      enemiesPieces,
      -1,
      -1
    ) ++ longRangeMovements( // second diagonal upward
      friendlyPieces,
      enemiesPieces,
      1,
      -1
    ) ++ longRangeMovements(
      friendlyPieces,
      enemiesPieces,
      -1,
      1
    )) // second diagonal downward
      .distinct

end Bishop
