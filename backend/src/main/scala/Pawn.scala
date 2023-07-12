/** King piece.
  *
  * A pawn is a piece that can only go forward (facing its forward from its
  * color pieces)
  *
  * If a pawn make it to the other side of the board, it promotes in either a
  * queen, a bishop, a knight or a rook.
  *
  * @param c
  *   The color of the piece.
  * @param p
  *   The position of the piece.
  */
class Pawn(c: String, p: Position) extends Piece(c, p):

  /** Indicates if the pawn can be taken en passant.
    */
  private var _enPassant: Boolean = false

  /** Indicates if en passant is possible for this pawn.
    *
    * @return
    *   true if it is possible, false otherwise.
    */
  def enPassant: Boolean = _enPassant
  def enPassant_(target: Boolean) =
    _enPassant = target

  override def toString(): String =
    if color == "B" then "♙"
    else "♟︎"

  /** The available movements of pawn.
    *
    * A pawn can move forward of one square if the path is clear, or capture an
    * enemy piece in its direct diagonal forward vicinity.
    *
    * If a pawn makes its first move, it can move two squares at once if the
    * path is clear.
    *
    * @todo
    *   implements en passant
    *
    * @param friendlyPieces
    *   Allies pieces.
    * @param enemiesPieces
    *   Enemies pieces.
    * @return
    *   Available movements of the pawn.
    */
  override def availableMovements(
      friendlyPieces: List[Piece],
      enemiesPieces: List[Piece]
  ): List[Position] =
    val direction: Int =
      if (color == "W") then 1 else -1

    val (row: Int, column: Int) = (position.row, position.column)
    val moveColumn: List[Position] =
      if (friendlyPieces ++ enemiesPieces)
          .filter(piece =>
            piece.position.column == column && piece.position.row == row + direction
          )
          .toList == List()
      then List(Position(row + direction, column))
      else List()

    val firstMove: List[Position] =
      if ((color == "W" && row == 1) || (color == "B" && row == 6))
        && (friendlyPieces ++ enemiesPieces)
          .filter(piece =>
            piece.position.column == column && (piece.position.row == row + direction
              || piece.position.row == row + 2 * direction)
          )
          .toList == List()
      then List(Position(row + 2 * direction, column))
      else List()

    val capturePiece: List[Position] =
      enemiesPieces
        .filter(piece =>
          piece.position.row == row + direction && (piece.position.column == column + 1 || piece.position.column == column - 1)
        )
        .map(_.position)

    val enPassant: List[Position] =
      enemiesPieces
        .filter(_.isInstanceOf[Pawn])
        .filter(_.asInstanceOf[Pawn].enPassant)
        .map(_.position)
        .filter(p =>
          (p.column == column + 1 || p.column == column - 1) && p.row == row
        )

    (moveColumn ++ capturePiece ++ firstMove ++ enPassant).filter(pos =>
      (pos.row <= 7) && (pos.row >= 0)
    )

  /** The pawn can promote when it reaches the last row.
    *
    * @param kind
    *   The piece name into which promotes.
    * @return
    *   The wanted piece.
    */
  def promote(kind: String): Piece =
    kind match
      case "Q" => Queen(color, position)
      case "B" => Bishop(color, position)
      case "R" => Rook(color, position)
      case "N" => Knight(color, position)

end Pawn
