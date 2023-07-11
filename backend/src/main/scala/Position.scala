/** @param x
  *   The row value
  * @param y
  *   The column value
  */
class Position(x: Int, y: Int):

  private var _row: Int = x
  def row: Int = _row
  def row_=(newValue: Int): Unit =
    _row = newValue

  private var _column: Int = y
  def column: Int = _column
  def column_=(newValue: Int): Unit =
    _column = newValue

  override def toString(): String =
    val outputColumn = ('a' to 'h').apply(column)
    val outPutRow = row + 1
    s"$outputColumn$outPutRow"

  def move(newRow: Int, newColumn: Int): Unit =
    _row = newRow
    _column = newColumn

  def move(newPos: Position): Unit =
    _row = newPos.row
    _column = newPos.column

  override def equals(x: Any): Boolean =
    x match {
      case x: Position => {
        (x.row == row) && (x.column == column)
      }
      case _ => false
    }

end Position
