class King(c: String, p: Position) extends Piece(c, p) with Castleable:

  override def toString(): String =
    if color == "B" then "♔"
    else "♚"

  def valideCastle(
      targetPosition: Position, // The position of the rook to castle with
      friendlyPieces: List[Piece],
      enemiesPieces: List[Piece]
  ): Boolean =

    val friendlyPiecesWithoutKing: List[Piece] =
      friendlyPieces.filter(_ != this)
    val direction: Int =
      if position.column > targetPosition.column then -1 else 1

    val debugMe = !isChecked(friendlyPiecesWithoutKing, enemiesPieces)

    (!isChecked(friendlyPiecesWithoutKing, enemiesPieces)
    && !King(color, Position(position.row, position.column + direction))
      .isChecked(friendlyPiecesWithoutKing, enemiesPieces)
    && !King(color, Position(position.row, position.column + 2 * direction))
      .isChecked(friendlyPiecesWithoutKing, enemiesPieces)
    && Rook(
      if color == "B" then "W" else "B",
      position
    ) // No obstacles between the king and the rook
      .availableMovements(enemiesPieces, friendlyPiecesWithoutKing)
      .contains(targetPosition))

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
    * This function allows to filter valid moves (one shall not put its own king
    * in check). A player is defeated if its king is in check and this player
    * has no valid moves. If the only move is to stay in place, it's Stalemate
    * (draw).
    *
    * @param friendlyPieces
    * @param enemiesPieces
    * @return
    */
  def isChecked(
      friendlyPieces: List[Piece],
      enemiesPieces: List[Piece]
  ): Boolean =
    var result = false

    // Check for Bishop and Queen
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
        .size > 0
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
        .size > 0
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
        .size > 0
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
        .size > 0
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
        .size > 0
    then result = true

    result

end King
