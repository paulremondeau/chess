// For more information on writing tests, see
// https://scalameta.org/munit/docs/getting-started.html
class PieceTest extends munit.FunSuite {

  test("Cannot make invalid moves") {

    val friendlyKing = King("W", Position(4, 4)) // e5

    val enemiesPieces: List[Piece] = List(Rook("B", Position(4, 0))) // Rook a5

    val friendlyPiece: Piece = Piece("W", Position(3, 3)) // d4

    // assertEquals(
    //   friendlyPiece
    //     .isValidMovement(Position(4, 3), List(), enemiesPieces, friendlyKing),
    //   true
    // )

    assertEquals(
      friendlyPiece
        .isValidMovement(Position(5, 3), List(), enemiesPieces, friendlyKing),
      false
    )

  }
}
