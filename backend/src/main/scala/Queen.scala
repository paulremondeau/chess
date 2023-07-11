/** Queen piece.
  *
  * A queen is a piece than can move on any squares of its diagonals, its row
  * and its columns.
  *
  * @param c
  *   The color of the piece.
  * @param p
  *   The position of the piece.
  */
class Queen(c: String, p: Position) extends Piece(c, p):

  override def toString(): String =
    if color == "B" then "♕"
    else "♛"

  /** Gives the available movements of the queen.
    *
    * @param friendlyPieces
    *   Allies pieces.
    * @param enemiesPieces
    *   Enemies pieces.
    * @return
    *   The available movements.
    */
  override def availableMovements(
      friendlyPieces: List[Piece],
      enemiesPieces: List[Piece]
  ): List[Position] =
    (longRangeMovements( // upward
      friendlyPieces,
      enemiesPieces,
      1,
      0
    ) ++ longRangeMovements( // downward
      friendlyPieces,
      enemiesPieces,
      -1,
      0
    ) ++ longRangeMovements( // rightward
      friendlyPieces,
      enemiesPieces,
      0,
      1
    ) ++ longRangeMovements( // leftward
      friendlyPieces,
      enemiesPieces,
      0,
      -1
    ) ++ (longRangeMovements( // first diagonal upward
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
    ))).distinct

end Queen
