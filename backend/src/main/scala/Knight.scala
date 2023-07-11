/** Knight piece.
  *
  * A knight is a piece with L movements that can jump through pieces.
  *
  * @param c
  *   The color of the piece.
  * @param p
  *   The position of the piece.
  */
class Knight(c: String, p: Position) extends Piece(c, p):

  override def toString(): String =
    if color == "B" then "♘"
    else "♞"

  /** Available movements of the knight.
    *
    * The knight can move in any L movement of height 1 and width 2.
    *
    * @param friendlyPieces
    *   Allies pieces.
    * @param enemiesPieces
    *   Enemies pieces.
    * @return
    *   The list of available movements.
    */
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
