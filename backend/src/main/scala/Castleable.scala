/** A piece that can castle.
  *
  * By default, castle is authorize.
  */
trait Castleable {

  private var _canCastle: Boolean = true
  def canCastle: Boolean = _canCastle

  /** Disable the right to castle for this piece.
    */
  def cannotCastle: Unit =
    _canCastle = false

}
