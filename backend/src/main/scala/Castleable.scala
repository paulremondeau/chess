trait Castleable {

  private var _canCastle: Boolean = true
  def canCastle: Boolean = _canCastle
  def cannotCastle: Unit =
    _canCastle = false

}
