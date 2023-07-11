// For more information on writing tests, see
// https://scalameta.org/munit/docs/getting-started.html
class BishopTest extends munit.FunSuite {

  test("Can move") {

    val bishop = Bishop("W", Position(4, 4))
    assertEquals(
      bishop.availableMovements(List(), List()),
      List(
        Position(5, 5),
        Position(6, 6),
        Position(7, 7),
        Position(3, 3),
        Position(2, 2),
        Position(1, 1),
        Position(0, 0),
        Position(5, 3),
        Position(6, 2),
        Position(7, 1),
        Position(3, 5),
        Position(2, 6),
        Position(1, 7)
      )
    )

  }

}
