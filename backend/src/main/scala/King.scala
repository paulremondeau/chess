/** King piece.
  *
  * A king is a piece which can move of one square in any direction.
  *
  * @param c
  *   The color of the piece.
  * @param p
  *   The position of the piece.
  */
class King(c: String, p: Position) extends Piece(c, p) with Castleable:

  override def toString(): String =
    if color == "B" then "♔"
    else "♚"

  /** Verify if the castle move is a valid one.
    *
    * This function verifies if the target position is correct for castleing.
    *
    * It first verify if the king is checked, and if any of the square it has to
    * move are under attack.
    *
    * If not, it verifies if there is no pieces between the king and the
    * targeted position.
    *
    * @param targetPosition
    *   The position on which casteling.
    * @param friendlyPieces
    *   The allies pieces.
    * @param enemiesPieces
    *   The enemies pieces.
    * @return
    *   true if the move is valid, false otherwise.
    */
  def valideCastle(
      targetPosition: Position,
      friendlyPieces: List[Piece],
      enemiesPieces: List[Piece]
  ): Boolean =

    val friendlyPiecesWithoutKing: List[Piece] =
      friendlyPieces.filter(_ != this)
    val direction: Int =
      if position.column > targetPosition.column then -1 else 1

    val debugMe = !isChecked(friendlyPiecesWithoutKing, enemiesPieces)

    (!isChecked(friendlyPiecesWithoutKing, enemiesPieces) // King is not checked
    && !King(color, Position(position.row, position.column + direction))
      .isChecked(
        friendlyPiecesWithoutKing,
        enemiesPieces
      ) // First square is not under attack
    && !King(color, Position(position.row, position.column + 2 * direction))
      .isChecked(
        friendlyPiecesWithoutKing,
        enemiesPieces
      ) // Second square is not under attack
    && Rook(
      if color == "B" then "W" else "B",
      position
    ) // No obstacles between the king and the rook
      .availableMovements(enemiesPieces, friendlyPiecesWithoutKing)
      .contains(targetPosition))

  /** Available movements of the king.
    *
    * The king can go in any directions for one square. The king can also castle
    * with one of its rooks, if it has the ability to do so.
    *
    * @param friendlyPieces
    *   The allies pieces.
    * @param enemiesPieces
    *   The enemies pieces.
    * @return
    *   The list of available movements.
    */
  override def availableMovements(
      friendlyPieces: List[Piece],
      enemiesPieces: List[Piece]
  ): List[Position] =
    val (row: Int, column: Int) = (position.row, position.column)

    val moves: List[Position] =
      List[Int](row - 1, row, row + 1)
        .map(x =>
          List[Int](column - 1, column, column + 1).map(y => (x, y))
        ) // All the squares around the king
        .fold(List[(Int, Int)]())((x, y) =>
          x ++ y
        ) // Concatenates to a single list
        .filter((x, y) =>
          !(x == row && y == column)
        ) // Remove the square where the king is
        .map((x, y) => Position(x, y)) // to position
        .filter(x =>
          !friendlyPieces.map(_.position).contains(x)
        ) // remove squares where friendly pieces are

    var castleRooks: List[Position] = List()
    if canCastle then // castle

      castleRooks = friendlyPieces
        .filter(p => p.isInstanceOf[Rook])
        .filter(_.asInstanceOf[Rook].canCastle)
        .map(_.position)
        .filter(p => valideCastle(p, friendlyPieces, enemiesPieces))

    moves.filter(pos =>
      ((pos.row).max(pos.column) <= 7) && ((pos.row).min(pos.column) >= 0)
    ) ++ castleRooks

  /** The king cannot move into check.
    *
    * This function need to be overwritten because the king is moving.
    * @todo
    *   find a way to remove friendlyKing and keeping it simple to use.
    *
    * @param targetPosition
    *   The target position for the king.
    * @param friendlyPieces
    *   Allies pieces.
    * @param enemiesPieces
    *   Enemies pieces.
    * @param friendlyKing
    *   Useless.
    * @return
    *   true if the movement is valid, false otherwise.
    */
  override def isValidMovement(
      targetPosition: Position,
      friendlyPieces: List[Piece],
      enemiesPieces: List[Piece],
      friendlyKing: King // useless
  ): Boolean =

    val futureEnemiesPieces: List[Piece] = enemiesPieces.filter(p =>
      p.position != targetPosition
    ) // If we capture, remove this piece

    val futureKing: King = King(color, targetPosition)

    val futurefriendlyPieces: List[Piece] =
      friendlyPieces.filter(_ != this) // Piece move, remove it
        ++ List(futureKing)

    !futureKing.isChecked(futurefriendlyPieces, futureEnemiesPieces)

  /** The king is checked if it is in any enemies pieces movements range.
    *
    * @param friendlyPieces
    *   Allies pieces.
    * @param enemiesPieces
    *   Enemies pieces.
    * @return
    *   true if the king is in check, false otherwise.
    */
  def isChecked(
      friendlyPieces: List[Piece],
      enemiesPieces: List[Piece]
  ): Boolean =
    var result = false

    // Check for Bishop and Queen attacks
    val bishopMovements: Set[Position] =
      Bishop(color, position)
        .availableMovements(friendlyPieces, enemiesPieces)
        .toSet

    if bishopMovements
        .intersect(
          enemiesPieces
            .filter(p => p.isInstanceOf[Bishop] || p.isInstanceOf[Queen])
            .map(_.position)
            .toSet
        )
        .size > 0 // At least one enemy bishop or queen is in the bishop's capture range
    then result = true

    // Check for Rook and Queen
    val rookMovements: Set[Position] =
      Rook(color, position)
        .availableMovements(friendlyPieces, enemiesPieces)
        .toSet

    if rookMovements
        .intersect(
          enemiesPieces
            .filter(p => p.isInstanceOf[Rook] || p.isInstanceOf[Queen])
            .map(_.position)
            .toSet
        )
        .size > 0 // At least one enemy rook or queen is in the rook's capture range
    then result = true

    // Check for Knight
    val knightMovements: Set[Position] =
      Knight(color, position)
        .availableMovements(friendlyPieces, enemiesPieces)
        .toSet

    if knightMovements
        .intersect(
          enemiesPieces
            .filter(p => p.isInstanceOf[Knight])
            .map(_.position)
            .toSet
        )
        .size > 0 // At least one enemy knight is in the knight's capture range
    then result = true

    // Check for Pawn
    val pawnMovements: Set[Position] = Pawn(color, position)
      .availableMovements(friendlyPieces, enemiesPieces)
      .toSet

    if pawnMovements
        .intersect(
          enemiesPieces
            .filter(p => p.isInstanceOf[Pawn])
            .map(_.position)
            .toSet
        )
        .size > 0 // At least one enemy pawn is in the pawn's capture range
    then result = true

    // Check for King
    val facticeKing = King(color, position)
    facticeKing.cannotCastle
    val kingMovements: Set[Position] =
      facticeKing.availableMovements(friendlyPieces, enemiesPieces).toSet
    if kingMovements
        .intersect(
          enemiesPieces
            .filter(p => p.isInstanceOf[King])
            .map(_.position)
            .toSet
        )
        .size > 0 // At least one enemy king is in the king's capture range
    then result = true

    result

end King
