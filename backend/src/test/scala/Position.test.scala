// For more information on writing tests, see
// https://scalameta.org/munit/docs/getting-started.html
class PositionTest extends munit.FunSuite {
  test("Positions are correctly read") {

    assertEquals(Position(4, 4).toString(), "e5")
    assertEquals(Position(1, 6).toString(), "g2")
    assertEquals(Position(6, 3).toString(), "d7")
    assertEquals(Position(0, 4).toString(), "e1")
    assertEquals(Position(3, 3).toString(), "d4")
    assertEquals(Position(1, 0).toString(), "a2")
    assertEquals(Position(0, 0).toString(), "a1")

  }

  test("Movement works") {

    val position = Position(0, 0)
    assertEquals(position.toString(), "a1")

    position.move(3, 2)
    assertEquals(position.toString(), "c4")

    position.move(6, 0)
    assertEquals(position.toString(), "a7")

  }
}
