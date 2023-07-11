trait Player:

  private var checked: Boolean = false
  private var timer: Int = 0

  private val _pieces: Array[Piece] = new Array[Piece](16)
  def pieces: Array[Piece] = _pieces

  // protected def initializePosition(pawnsRow: Int, piecesRow: Int): Unit =
  //   pieces(0) = new Pawn(pawnsRow, 1)
  //   pieces(1) = new Pawn(pawnsRow, 2)
  //   pieces(2) = new Pawn(pawnsRow, 3)
  //   pieces(3) = new Pawn(pawnsRow, 4)
  //   pieces(4) = new Pawn(pawnsRow, 5)
  //   pieces(5) = new Pawn(pawnsRow, 6)
  //   pieces(6) = new Pawn(pawnsRow, 7)
  //   pieces(7) = new Pawn(pawnsRow, 8)
  //   pieces(8) = new Rook(piecesRow, 1)
  //   pieces(9) = new Knight(piecesRow, 2)
  //   pieces(10) = new Bishop(piecesRow, 3)
  //   pieces(11) = new Queen(piecesRow, 4)
  //   pieces(12) = new King(piecesRow, 5)
  //   pieces(13) = new Bishop(piecesRow, 6)
  //   pieces(14) = new Knight(piecesRow, 7)
  //   pieces(15) = new Rook(piecesRow, 8)

  // def initPosition(): Unit

  // def describe(): String =
  //   pieces.mkString(", ")

end Player
