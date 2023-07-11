/** Bishop piece.
  *
  * A bishop is a piece with long rang movements on diagonals.
  *
  * @param c
  *   The color of the piece.
  * @param p
  *   The position of the piece.
  */
class Bishop(c: String, p: Position) extends Piece(c, p):

  override def toString(): String =
    if color == "B" then "♗"
    else "♝"

  /** Available movements of the piece.
    *
    * The bishop can go to all the squares of its diagonals, until it reaches
    * another piece. If this piece is of another color, it can capture it.
    *
    * @param friendlyPieces
    *   The allies pieces.
    * @param enemiesPieces
    *   The enemies pieces.
    * @return
    *   The list of square the bishop can go.
    */
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
