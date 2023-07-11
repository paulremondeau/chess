/** Rook piece.
  *
  * A rook is a piece than can move on any squares of its row and its columns.
  *
  * @param c
  *   The color of the piece.
  * @param p
  *   The position of the piece.
  */
class Rook(c: String, p: Position) extends Piece(c, p) with Castleable:

  override def toString(): String =
    if color == "B" then "♖"
    else "♜"

  /** Gives the available movements of the rook.
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
    ) ++ longRangeMovements(friendlyPieces, enemiesPieces, 0, -1)) // leftward
      .distinct

end Rook
