// For more information on writing tests, see
// https://scalameta.org/munit/docs/getting-started.html
class KingTest extends munit.FunSuite {
  test("Can move") {

    val king = King("W", Position(7, 0))
    assertEquals(
      king.availableMovements(List(), List()),
      List(Position(6, 0), Position(6, 1), Position(7, 1))
    )

  }

  test("Cannot make invalid moves") {

    val king = King("W", Position(4, 4)) // e5

    val enemiesPieces: List[Piece] = List(Rook("B", Position(3, 0))) // Rook a5

    val friendlyPiece: Piece = Piece("W", Position(3, 3)) // d4

    assertEquals(
      king
        .isValidMovement(Position(3, 4), List(), enemiesPieces, king),
      false
    )

  }

  test("Castle is possible") {

    val king = King("W", Position(0, 4)) // e1

    assertEquals(
      king
        .valideCastle(Position(0, 7), List(Rook("W", Position(0, 7))), List()),
      true
    )
  }

  test("Castle cannot go through pieces") {

    val king = King("W", Position(0, 4)) // e1
    assertEquals(
      king
        .valideCastle(
          Position(0, 7),
          List(Rook("W", Position(0, 7)), Piece("W", Position(0, 6))),
          List()
        ),
      false
    )

    assertEquals(
      king
        .valideCastle(
          Position(0, 0),
          List(
            Rook("W", Position(0, 0)),
            Piece("W", Position(0, 1)),
            Piece("W", Position(0, 2)),
            Piece("W", Position(0, 3))
          ),
          List()
        ),
      false
    )

    assertEquals(
      king
        .valideCastle(
          Position(0, 0),
          List(
            Rook("W", Position(0, 0))
          ),
          List(Piece("B", Position(0, 2)))
        ),
      false
    )
  }

  test("Cannot castle if one square is attacked") {

    val king = King("W", Position(0, 4)) // e1

    assertEquals( // Rook attacks the square next to the king
      king
        .valideCastle(
          Position(0, 0),
          List(
            Rook("W", Position(0, 0))
          ),
          List(Rook("B", Position(7, 3)))
        ),
      false
    )

    assertEquals( // Rook attacks square next to rook but king will not go to this square -> castle possible
      king
        .valideCastle(
          Position(0, 0),
          List(
            Rook("W", Position(0, 0))
          ),
          List(Rook("B", Position(7, 1)))
        ),
      true
    )

  }

  test("Can castle when it's possible") {

    val king = King("W", Position(0, 4)) // e1
    val targetRook = Rook("W", Position(0, 7)) // h1

    assertEquals(
      king.availableMovements(
        List(
          targetRook,
          Rook("W", Position(0, 0)),
          Piece("W", Position(0, 3)),
          Piece("W", Position(1, 4)),
          Piece("W", Position(1, 3)),
          Piece("W", Position(1, 5))
        ),
        List()
      ),
      List(Position(0, 5), Position(0, 7))
    )

    assertEquals(
      king.availableMovements(
        List(
          targetRook,
          Piece("W", Position(0, 3)),
          Piece("W", Position(1, 4)),
          Piece("W", Position(1, 3)),
          Piece("W", Position(1, 5))
        ),
        List()
      ),
      List(Position(0, 5), Position(0, 7))
    )

  }

  test("Cannot castle after moving") {

    val king = King("W", Position(0, 4)) // e1
    assertEquals(king.canCastle, true)

    king.move(1, 1)
    assertEquals(king.canCastle, false)

  }

  test("G4 was checkmate for white although it wasn't...") {

    val board: Board = Board()
    board.initialize()

    // White pieces
    board.board(1)(4) = Piece("_", Position(1, 4))

    board.movePiece(board.board(1)(7), Position(3, 7))

    board.board(3)(7) = Pawn("W", Position(3, 7))
    board.board(5)(2) = Queen("W", Position(5, 2))

    // Black pieces
    board.board(7)(2) = Piece("_", Position(7, 2))
    board.board(7)(4) = Piece("_", Position(7, 4))
    board.board(6)(2) = Piece("_", Position(6, 2))
    board.board(6)(3) = Piece("_", Position(6, 3))
    board.board(6)(4) = Piece("_", Position(6, 4))

    board.board(4)(2) = Pawn("B", Position(4, 2))
    board.board(4)(4) = Pawn("B", Position(4, 4))

    val king: King = King("B", Position(4, 5))
    board.board(4)(5) = king

    board.movePiece(board.board(1)(6), Position(3, 6))
    assertEquals(
      board.toString(),
      """
  ---------------------------------
8 | ♖ | ♘ |   | ♕ |   | ♗ | ♘ | ♖ |
  ---------------------------------
7 | ♙ | ♙ |   |   |   | ♙ | ♙ | ♙ |
  ---------------------------------
6 |   |   | ♛ |   |   |   |   |   |
  ---------------------------------
5 |   |   | ♙ |   | ♙ | ♔ |   |   |
  ---------------------------------
4 |   |   |   |   |   |   | ♟︎ | ♟︎ |
  ---------------------------------
3 |   |   |   |   |   |   |   |   |
  ---------------------------------
2 | ♟︎ | ♟︎ | ♟︎ | ♟︎ |   | ♟︎ |   |   |
  ---------------------------------
1 | ♜ | ♞ | ♝ | ♛ | ♚ | ♝ | ♞ | ♜ |
  ---------------------------------
    a   b   c   d   e   f   g   h"""
    )

    val friendlyPieces: List[Piece] = board.board
      .map(x => x.filter(y => y.color == "B"))
      .fold(Array[Piece]())((x, y) => x ++ y)
      .toList

    val enemiesPieces: List[Piece] = board.board
      .map(x => x.filter(y => y.color == "W"))
      .fold(Array[Piece]())((x, y) => x ++ y)
      .toList

    assertEquals(
      king
        .availableMovements(friendlyPieces, enemiesPieces)
        .filter(p =>
          king.isValidMovement(p, friendlyPieces, enemiesPieces, king)
        ),
      List(Position(3, 5))
    )

    val availableMovements = friendlyPieces
      .map(x =>
        x -> x
          .availableMovements(friendlyPieces, enemiesPieces)
          .filter(y =>
            x.isValidMovement(
              y,
              friendlyPieces,
              enemiesPieces,
              king
            )
          )
      )
      .toMap

    assertEquals(availableMovements.map((x, y) => y.length).sum, 1)

    board.board(3)(6).asInstanceOf[Pawn].enPassant_(true)

    assertEquals(
      king
        .availableMovements(friendlyPieces, enemiesPieces)
        .filter(p =>
          king.isValidMovement(p, friendlyPieces, enemiesPieces, king)
        ),
      List(Position(3, 5))
    )

    assertEquals(availableMovements.map((x, y) => y.length).sum, 1)

  }

}
