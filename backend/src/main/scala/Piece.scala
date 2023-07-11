/** A piece architecture. This is not an abstract class because empty squares
  * will be filled with piece with the "_" color.
  *
  * @param c
  *   The color of the piece
  * @param p
  *   The position of the piece
  */
class Piece(c: String, p: Position):

  private var _color: String = c
  def color: String = _color

  private var _position: Position = p
  def position: Position = _position

  /** Overriding the toString method.
    *
    * Neutral piece will be printed as blank space.
    *
    * @return
    *   The blank space
    */
  override def toString(): String = " "

  def availableMovements(
      friendlyPieces: List[Piece],
      enemiesPieces: List[Piece]
  ): List[Position] = List(Position(1, 1))

  /** A piece cannot move if it puts its king in check.
    *
    * This function filters valid moves from invalid ones. It verifies if the
    * king is not the direct target of enemies pieces once the piece has moved.
    *
    * The function does not modify any of the parameters, neither the piece
    * itself.
    *
    * @param targetPosition
    *   The square the piece wants to go.
    * @param friendlyPieces
    *   The allies pieces.
    * @param enemiesPieces
    *   The enemies pieces.
    * @param friendlyKing
    *   The ally king.
    * @return
    *   true if the move is valid, false otherwise.
    */
  def isValidMovement(
      targetPosition: Position,
      friendlyPieces: List[Piece],
      enemiesPieces: List[Piece],
      friendlyKing: King
  ): Boolean =

    // If the piece captures another one, the enemy piece must be removed.
    val futureEnemiesPieces: List[Piece] =
      enemiesPieces.filter(p => p.position != targetPosition)

    // The piece moves, therefore it leaves its position to the new one
    val futurefriendlyPieces: List[Piece] =
      friendlyPieces.filter(_ != this)
        ++ List(new Piece(color, targetPosition))

    // Verify if the ally king is not check with this new pieces disposition.
    !friendlyKing.isChecked(futurefriendlyPieces, futureEnemiesPieces)

  /** Move a piece.
    *
    * This function moves a piece by moving its given position.
    *
    * @param newPos
    */
  def move(newPos: Position): Unit =
    _position.move(newPos)
    if this.isInstanceOf[Castleable]
    then // If piece is able to castle, remove its right to do so after it has move
      this.asInstanceOf[Castleable].cannotCastle

  def move(newRow: Int, newColumn: Int): Unit =
    move(Position(newRow, newColumn))

  /** Movements for rooks, bishops and queens.
    */
  val longRangeMovements = (
      friendlyPieces: List[Piece],
      enemiesPieces: List[Piece],
      dRow: Int,
      dColumn: Int
  ) =>

    var (beforeList: List[Position], afterList: List[Position]) =
      (1 to 7)
        .map(x =>
          Position(position.row + dRow * x, position.column + dColumn * x)
        ) //
        .filter(p =>
          (p.row).min(p.column) >= 0 && (p.row).max(p.column) <= 7
        ) // Don't go outside the board !
        .toList
        .span(p =>
          !(friendlyPieces ++ enemiesPieces).map(_.position).contains(p)
        ) // Keep until we encounter a piece

    if afterList.length > 0 then // We encountered a friendly or enemy piece
      if (enemiesPieces.map(_.position)).contains(afterList.head)
      then // This was an enemy piece, we can capture it
        beforeList ++ List(afterList.head)
      else beforeList
    else beforeList

end Piece
