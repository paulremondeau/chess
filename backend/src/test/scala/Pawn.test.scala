// For more information on writing tests, see
// https://scalameta.org/munit/docs/getting-started.html
class PawnTest extends munit.FunSuite {
  test("It shows correctly") {

    val pawn1 = Pawn("B", Position(1, 1))
    assertEquals(pawn1.toString(), "♙")

    val pawn2 = Pawn("W", Position(1, 1))
    assertEquals(pawn2.toString(), "♟︎")

  }

  test("White can move correctly") {

    val whitePawn = Pawn("W", Position(0, 0))
    assertEquals(
      whitePawn.availableMovements(List(), List()),
      List(Position(1, 0))
    )

    whitePawn.move(6, 0)
    assertEquals(
      whitePawn.availableMovements(List(), List()),
      List(Position(7, 0))
    )

    // Cannot go through allies
    whitePawn.move(6, 0)
    assertEquals(
      whitePawn.availableMovements(List(Piece("W", Position(7, 0))), List()),
      List()
    )

    // Cannot leave board
    whitePawn.move(7, 0)
    assertEquals(
      whitePawn.availableMovements(List(), List()),
      List()
    )

    // Can move two squares if first move
    whitePawn.move(1, 1)
    assertEquals(
      whitePawn.availableMovements(List(), List()),
      List(Position(2, 1), Position(3, 1))
    )

    assertEquals(
      whitePawn.availableMovements(List(Piece("W", Position(3, 1))), List()),
      List(Position(2, 1))
    )

    assertEquals(
      whitePawn.availableMovements(List(Piece("B", Position(2, 1))), List()),
      List()
    )

    // Can capture enemies pieces
    whitePawn.move(6, 1)
    assertEquals(
      whitePawn.availableMovements(
        List(Piece("W", Position(7, 1))),
        List(
          Piece("B", Position(7, 0)),
          Piece("B", Position(7, 2)),
          Piece("B", Position(5, 2)),
          Piece("B", Position(5, 0)),
          Piece("B", Position(6, 2)),
          Piece("B", Position(1, 2))
        )
      ),
      List(Position(7, 0), Position(7, 2))
    )

  }

  test("Black can move correctly") {

    val blackPawn = Pawn("B", Position(7, 0))
    assertEquals(
      blackPawn.availableMovements(List(), List()),
      List(Position(6, 0))
    )

    blackPawn.move(1, 0)
    assertEquals(
      blackPawn.availableMovements(List(), List()),
      List(Position(0, 0))
    )

    // Cannot go through allies
    blackPawn.move(6, 0)
    assertEquals(
      blackPawn.availableMovements(List(Piece("B", Position(5, 0))), List()),
      List()
    )

    // Cannot leave board
    blackPawn.move(0, 0)
    assertEquals(
      blackPawn.availableMovements(List(), List()),
      List()
    )

    // Can move two squares if first move
    blackPawn.move(6, 1)
    assertEquals(
      blackPawn.availableMovements(List(), List()),
      List(Position(5, 1), Position(4, 1))
    )

    assertEquals(
      blackPawn.availableMovements(List(Piece("B", Position(4, 1))), List()),
      List(Position(5, 1))
    )

    assertEquals(
      blackPawn.availableMovements(List(Piece("W", Position(5, 1))), List()),
      List()
    )

    // Can capture enemies pieces
    blackPawn.move(6, 1)
    assertEquals(
      blackPawn.availableMovements(
        List(Piece("B", Position(5, 1))),
        List(
          Piece("W", Position(5, 0)),
          Piece("W", Position(5, 2)),
          Piece("W", Position(7, 0)),
          Piece("W", Position(6, 2)),
          Piece("W", Position(1, 2))
        )
      ),
      List(Position(5, 0), Position(5, 2))
    )

  }

  // Make sure bugs now works
  test("Can promote") {

    val enemiesPieces: List[Piece] =
      (0 to 7).map(y => Pawn("W", Position(1, y))).toList ++ List[Piece](
        Rook("W", Position(0, 0)),
        Knight("W", Position(0, 1)),
        Bishop("W", Position(0, 2)),
        Queen("W", Position(0, 3)),
        King("W", Position(0, 4)),
        Bishop("W", Position(0, 5)),
        Knight("W", Position(0, 6)),
        Rook("W", Position(0, 7))
      )

    val blackPawn = Pawn("B", Position(2, 3))
    assertEquals(
      blackPawn.availableMovements(
        List(),
        enemiesPieces
      ),
      List(Position(1, 2), Position(1, 4))
    )

  }

}
