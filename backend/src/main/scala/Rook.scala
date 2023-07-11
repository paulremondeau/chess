import scala.util.control._

class Rook(c: String, p: Position) extends Piece(c, p) with Castleable:

  override def toString(): String =
    if color == "B" then "♖"
    else "♜"

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
