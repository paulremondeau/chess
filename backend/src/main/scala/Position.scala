/** A position in the chess board.
  *
  * @param x
  *   The row value. Must be 0 <= x <= 7.
  * @param y
  *   The column value. Must be 0 <= y <= 7
  */
class Position(x: Int, y: Int):

  private var _row: Int = x

  /** Get row value.
    *
    * @return
    *   Row value
    */
  def row: Int = _row

  private var _column: Int = y

  /** Get column value.
    *
    * @return
    */
  def column: Int = _column

  override def toString(): String =
    val outputColumn = ('a' to 'h').apply(column)
    val outPutRow = row + 1
    s"$outputColumn$outPutRow"

  /** Move the position to another square.
    *
    * @param newRow
    *   The new row.
    * @param newColumn
    *   The new column.
    */
  def move(newRow: Int, newColumn: Int): Unit =
    _row = newRow
    _column = newColumn

  /** Move the position to another square.
    *
    * @param newPos
    *   The new position
    */
  def move(newPos: Position): Unit =
    _row = newPos.row
    _column = newPos.column

  /** Two positions are equals if both rows are the same and both columns are
    * the same.
    *
    * @param x
    *   The object to compare.
    * @return
    *   The result of the equality.
    */
  override def equals(x: Any): Boolean =
    x match {
      case x: Position => {
        (x.row == row) && (x.column == column)
      }
      case _ => false
    }

end Position
