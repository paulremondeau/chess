/** Neutral empty piece, to fill the blanks in the board
  */
class Piece(c: String, p: Position):

  private var _color: String = c
  def color: String = _color

  private var _position: Position = p
  def position: Position = _position

  override def toString(): String = " "

  def availableMovements(
      friendlyPieces: List[Piece],
      enemiesPieces: List[Piece]
  ): List[Position] = List(Position(1, 1))

  def isValidMovement(
      targetPosition: Position,
      friendlyPieces: List[Piece],
      enemiesPieces: List[Piece],
      friendlyKing: King
  ): Boolean =

    val futureEnemiesPieces: List[Piece] = enemiesPieces.filter(p =>
      p.position != targetPosition
    ) // If we capture, remove this piece

    val futurefriendlyPieces: List[Piece] =
      friendlyPieces.filter(_ != this) // Piece move, remove it
        ++ List(new Piece(color, targetPosition))

    !friendlyKing.isChecked(futurefriendlyPieces, futureEnemiesPieces)

  def move(newRow: Int, newColumn: Int): Unit =
    _position.move(newRow, newColumn)
    if this.isInstanceOf[Castleable]
    then // If piece is able to castle, remove its right to do so
      this.asInstanceOf[Castleable].cannotCastle

  def move(newPos: Position): Unit =
    _position.move(newPos)
    if this.isInstanceOf[Castleable]
    then // If piece is able to castle, remove its right to do so
      this.asInstanceOf[Castleable].cannotCastle

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
