/** A game of chess.
  *
  * A game is played between two players, white and black.
  * @todo
  *   add clocks
  * @todo
  *   keep move orders somewhere
  * @todo
  *   draw rules (50 moves, 3 positions)
  */
class GameAPI:

  /** The winner of the game
    *
    * While its the empty string, the game has no winner.
    */
  private var _winner: String = ""

  /** Access the winner of the game.
    *
    * If it's a stalemate, the winner is set to "_"
    *
    * @return
    *   The winner.
    */
  def winner: String = _winner

  /** Which player it is to play
    */
  private var _turn: String = "W"
  def turn: String = _turn

  /** The player of the next turn.
    */
  private var nextTurn: String = "B"

  /** @todo
    *   used to store the moves order.
    */
  private var moves: List[Any] = List() // Used to store the moves

  /** The chess board.
    */
  private val _board: Board = Board()

  /** Access the board of the game.
    *
    * @return
    *   The board.
    */
  def board: Board = _board
  _board.initialize()

  /** A list of all the pieces of the given color.
    */
  val colorPiece = (color: String) =>
    _board.board
      .map(x => x.filter(y => y.color == color))
      .fold(Array[Piece]())((x, y) => x ++ y)
      .toList

  /** Check if the input position is of the right format.
    *
    * This function checks if the given position is a correct chess square, i.e
    * of the form a1 -> h8
    *
    * @param input
    *   The given input.
    * @return
    *   true if the input is a correct position, false otherwise.
    */
  def validPosition(input: String): Boolean =
    if input.length() == 2 then
      if ('a' to 'h').contains(input(0)) then
        if ('1' to '8').contains(input(1)) then true
        else false
      else false
    else false

  /** Convert a string position to its int tuple equivalent.
    *
    * The given string is suppose to already be of good quality.
    *
    * @param input
    *   The string position to convert.
    * @return
    *   The square coordinate in matrix conventions.
    */
  def convertPos(input: String): (Int, Int) =

    val row: Char = input(1)
    val column: Char = input(0)

    val numericRow: Int = row.asDigit - 1
    val numericColumn: Int = ('a' to 'z').indexOf(column)

    (numericRow, numericColumn)

  def convertForFrontend(): Map[String, PieceInformation] =
    val friendlyPieces: List[Piece] = colorPiece(_turn)
    val friendlyKing: King =
      friendlyPieces.filter(p => p.isInstanceOf[King]).head.asInstanceOf[King]

    val enemiesPieces: List[Piece] = colorPiece(nextTurn)
    val enemyKing: King =
      enemiesPieces.filter(p => p.isInstanceOf[King]).head.asInstanceOf[King]

    (friendlyPieces
      .map(x =>
        x.position.toString() ->
          PieceInformation(
            pieceType = x.getClass.getName,
            pieceColor = x.color.toLowerCase(),
            availableMovements = x
              .availableMovements(friendlyPieces, enemiesPieces)
              .filter(y =>
                x.isValidMovement(
                  y,
                  friendlyPieces,
                  enemiesPieces,
                  friendlyKing
                )
              )
              .map(_.toString()),
            isChecked =
              if x.isInstanceOf[King] then
                x.asInstanceOf[King].isChecked(friendlyPieces, enemiesPieces)
              else false
          )
      ) ++ enemiesPieces
      .map(x =>
        x.position.toString() ->
          PieceInformation(
            pieceType = x.getClass.getName,
            pieceColor = x.color.toLowerCase(),
            availableMovements = x
              .availableMovements(enemiesPieces, friendlyPieces)
              .filter(y =>
                x.isValidMovement(
                  y,
                  enemiesPieces,
                  friendlyPieces,
                  enemyKing
                )
              )
              .map(_.toString()),
            isChecked =
              if x.isInstanceOf[King] then
                x.asInstanceOf[King].isChecked(enemiesPieces, friendlyPieces)
              else false
          )
      )).toMap

    // (friendlyPieces
    //   .map(x =>
    //     x.position.toString() ->
    //       Map[String, String | List[String]](
    //         "name" -> x.getClass().toString(),
    //         "color" -> x.color.toLowerCase(),
    //         "movements" -> x
    //           .availableMovements(friendlyPieces, enemiesPieces)
    //           .filter(y =>
    //             x.isValidMovement(
    //               y,
    //               friendlyPieces,
    //               enemiesPieces,
    //               friendlyKing
    //             )
    //           )
    //           .map(_.toString())
    //       )
    //   ) ++ enemiesPieces
    //   .map(x =>
    //     x.position.toString() ->
    //       Map[String, String | List[String]](
    //         "name" -> x.getClass().toString(),
    //         "color" -> x.color.toLowerCase(),
    //         "movements" -> x
    //           .availableMovements(enemiesPieces, friendlyPieces)
    //           .filter(y =>
    //             x.isValidMovement(
    //               y,
    //               enemiesPieces,
    //               friendlyPieces,
    //               enemyKing
    //             )
    //           )
    //           .map(_.toString())
    //       )
    //   )).toMap

  /** Play a turn of chess.
    *
    * The function start by verifying if the game is not over (checkmate or
    * stalemate).
    *
    * If not, the player of which it's the turn pick a piece and then pick a
    * position to move this piece.
    */
  def play(
      selectedSquare: String,
      targetSquare: String,
      promotion: Option[String]
  ): Map[String, PieceInformation] =

    val selectedRow = convertPos(selectedSquare)(0)
    val selectedColumn = convertPos(selectedSquare)(1)
    val selectedPiece: Piece = _board.board(selectedRow)(selectedColumn)

    val selectedRowMove = convertPos(targetSquare)(0)
    val selectedColumnMove = convertPos(targetSquare)(1)
    val selectedPos = Position(selectedRowMove, selectedColumnMove)

    selectedPiece match
      case pawn: Pawn =>
        if (pawn.color == "B" && selectedRowMove == 0) || (pawn.color == "W" && selectedRowMove == 7) // pawn can promote
        then

          val newPiece = pawn.promote(promotion.get)
          _board.movePiece(
            pawn,
            selectedPos
          ) // first move pawn...
          _board.movePiece(
            newPiece,
            selectedPos
          ) // ... then replace it by its promotion
        else if (pawn.position.row == selectedPos.row)
        then // en passant
          val direction = if pawn.color == "W" then 1 else -1
          _board.movePiece(
            pawn,
            Position(selectedPos.row + direction, selectedPos.column)
          ) // move pawn en passant

          _board.board(selectedPos.row)(selectedPos.column) =
            Piece("_", selectedPos) // remove captured pawn
        else // move normally

          if (selectedPos.row - pawn.position.row).abs > 1 then
            pawn.enPassant_(true)

          _board.movePiece(
            pawn,
            selectedPos
          )

      case king: King =>
        if king.canCastle then
          val castleableRooks: List[Piece] = colorPiece(_turn)
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

            _board.movePiece(
              castleRook,
              Position(
                king.position.row,
                king.position.column + direction
              )
            )

            _board.movePiece(
              king,
              Position(
                king.position.row,
                king.position.column + 2 * direction
              )
            )
          else
            _board.movePiece(
              king,
              selectedPos
            )
        else
          _board.movePiece(
            king,
            selectedPos
          )
      case _ => // move normally
        _board.movePiece(
          selectedPiece,
          selectedPos
        )

    colorPiece(nextTurn)
      .filter(_.isInstanceOf[Pawn])
      .map(
        _.asInstanceOf[Pawn].enPassant_(false)
      ) // Disable en passant for all enemies pawns

    val temp: String = nextTurn
    nextTurn = _turn
    _turn = temp

    val friendlyPieces: List[Piece] = colorPiece(_turn)
    val friendlyKing: King =
      friendlyPieces.filter(p => p.isInstanceOf[King]).head.asInstanceOf[King]

    val enemiesPieces: List[Piece] = colorPiece(nextTurn)
    val enemyKing: King =
      enemiesPieces.filter(p => p.isInstanceOf[King]).head.asInstanceOf[King]

    val availableMovements: Map[Piece, List[Position]] =
      friendlyPieces
        .map(x =>
          x -> x
            .availableMovements(friendlyPieces, enemiesPieces)
            .filter(y =>
              x.isValidMovement(
                y,
                friendlyPieces,
                enemiesPieces,
                friendlyKing
              )
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
      else _winner = "_" // Stalemate

    convertForFrontend()

end GameAPI
