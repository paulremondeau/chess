import scala.io.StdIn.readLine

class Game:

  private var _winner: String = ""
  def winner: String = _winner

  var turn: String = "W"
  var nextTurn: String = "B"
  var moves: List[Any] = List() // Used to store the moves
  val board: Board = Board()
  board.initialize()

  var nextTurnBoard: Board = Board() // Used to filter available movements

  val colorPiece = (color: String) =>
    board.board
      .map(x => x.filter(y => y.color == color))
      .fold(Array[Piece]())((x, y) => x ++ y)
      .toList

  def validPosition(input: String): Boolean =
    if input.length() == 2 then
      if ('a' to 'h').contains(input(0)) then
        if ('1' to '8').contains(input(1)) then true
        else false
      else false
    else false

  def play(): Unit =

    val friendlyPieces: List[Piece] = colorPiece(turn)
    val friendlyKing: King =
      friendlyPieces.filter(p => p.isInstanceOf[King])(0).asInstanceOf[King]

    val enemiesPieces: List[Piece] = colorPiece(nextTurn)
    val enemyKing: King =
      enemiesPieces.filter(p => p.isInstanceOf[King])(0).asInstanceOf[King]

    val availableMovements: Map[Piece, List[Position]] =
      friendlyPieces
        .map(x =>
          x -> x
            .availableMovements(friendlyPieces, enemiesPieces)
            .filter(y =>
              x.isValidMovement(y, friendlyPieces, enemiesPieces, friendlyKing)
            )
        )
        .toMap

    if availableMovements.map((x, y) => y.length).sum == 0
    then // No valid moves available
      if friendlyKing.isChecked(
          friendlyPieces,
          enemiesPieces
        ) // King is checked
      then _winner = nextTurn // Checkmate
      else _winner == "_" // Stalemate
    else // Game goes on
      println("It's " + turn + " turn to play.")
      println(board.visualizeBoard(turn))

      var playerHasPlay: Boolean = false
      var selectedSquare = ""
      var (selectedRow: Int, selectedColumn: Int) = (0, 0)
      var validSquare = false

      while (!playerHasPlay) {
        selectedSquare = ""
        selectedRow = 0
        selectedColumn = 0
        validSquare = false

        while (!validSquare) {
          println("Choose a piece to move :")
          selectedSquare = readLine()
          if validPosition(selectedSquare) then
            selectedRow = convertPos(selectedSquare)(0)
            selectedColumn = convertPos(selectedSquare)(1)
            if (selectedRow
                .min(selectedColumn) >= 0) && (selectedRow.max(
                selectedColumn
              ) <= 7) && board.board(selectedRow)(selectedColumn).color == turn
            then validSquare = true
            else validSquare = false
          else validSquare = false
        }

        var selectedPiece: Piece = board.board(selectedRow)(selectedColumn)
        var movs = availableMovements.apply(selectedPiece)
        println("Select movements available : " + movs)

        if movs.length > 0 then

          var (selectedRowMove: Int, selectedColumnMove: Int) = (0, 0)
          val selectedPos: Position = Position(0, 0)
          validSquare = false

          while (!validSquare) {
            println(
              "Choose a square to move, or enter (x) to choose another piece :"
            )
            selectedSquare = readLine()
            if selectedSquare == "x" then validSquare = true
            else if validPosition(selectedSquare) then
              selectedRowMove = convertPos(selectedSquare)(0)
              selectedColumnMove = convertPos(selectedSquare)(1)
              selectedPos.move(selectedRowMove, selectedColumnMove)
              if movs.contains(selectedPos) then validSquare = true
              else validSquare = false
            else validSquare = false
          }

          if selectedSquare == "X" then playerHasPlay = false
          else

            val selectedPiece = board.board(selectedRow)(selectedColumn)

            selectedPiece match
              case piece: Pawn =>
                if (piece.color == "B" && selectedRowMove == 0) || (piece.color == "W" && selectedRowMove == 7) // pawn can promote
                then
                  println("Choose how to promote your pawn (Q, R, B, N): ")
                  var promotion = readLine()
                  while (
                    !List[String]("Q", "B", "R", "N").contains(promotion)
                  ) {
                    promotion = readLine()
                  }
                  val newPiece = piece.promote(promotion)
                  board.movePiece(
                    piece,
                    selectedPos
                  ) // first move pawn...
                  board.movePiece(
                    newPiece,
                    selectedPos
                  ) // ... then replace it by its promotion
                else // move normally
                  board.movePiece(
                    piece,
                    selectedPos
                  )

              case king: King =>
                if king.canCastle then
                  val castleableRooks: List[Piece] = friendlyPieces
                    .filter(_.isInstanceOf[Rook])
                    .filter(_.asInstanceOf[Rook].canCastle)
                  if castleableRooks
                      .map(_.position)
                      .contains(selectedPos)
                  then
                    // It's a castle
                    val castleRook: Rook =
                      castleableRooks
                        .filter(p => p.position == selectedPos)
                        .head
                        .asInstanceOf[Rook]
                    val direction: Int =
                      if selectedPos.column > king.position.column then 1
                      else -1

                    board.movePiece(
                      castleRook,
                      Position(
                        king.position.row,
                        king.position.column + direction
                      )
                    )

                    board.movePiece(
                      king,
                      Position(
                        king.position.row,
                        king.position.column + 2 * direction
                      )
                    )
                  else
                    board.movePiece(
                      king,
                      selectedPos
                    )
                else
                  board.movePiece(
                    king,
                    selectedPos
                  )
              case _ => // move normally
                board.movePiece(
                  selectedPiece,
                  selectedPos
                )

            val temp: String = nextTurn
            nextTurn = turn
            turn = temp
            playerHasPlay = true
        else
          println("No available movements.")
          playerHasPlay = false
      }

end Game
