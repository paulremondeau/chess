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

}
