/** A piece that can castle.
  *
  * By default, castle is authorize.
  */
trait Castleable {

  private var _canCastle: Boolean = true

  /** Says if a piece is able to castle.
    *
    * @return
    *   true if the piece can castle, false otherwise.
    */
  def canCastle: Boolean = _canCastle

  /** Disable the right to castle for this piece.
    */
  def cannotCastle: Unit =
    _canCastle = false

}
