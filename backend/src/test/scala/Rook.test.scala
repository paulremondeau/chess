// For more information on writing tests, see
// https://scalameta.org/munit/docs/getting-started.html
class RookTest extends munit.FunSuite {
  test("Can move") {

    val king = Rook("W", Position(4, 4))
    assertEquals(
      king.availableMovements(List(), List()),
      List(
        Position(5, 4),
        Position(6, 4),
        Position(7, 4),
        Position(3, 4),
        Position(2, 4),
        Position(1, 4),
        Position(0, 4),
        Position(4, 5),
        Position(4, 6),
        Position(4, 7),
        Position(4, 3),
        Position(4, 2),
        Position(4, 1),
        Position(4, 0)
      )
    )

  }

  test("Cannot castle after moving") {

    val rook = Rook("W", Position(0, 4)) // e1
    assertEquals(rook.canCastle, true)

    rook.move(1, 1)
    assertEquals(rook.canCastle, false)

  }

}
