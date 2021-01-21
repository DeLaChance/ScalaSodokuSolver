package sudoku.utils

object IntegerUtils {

  def toInt(stringValue: String): Option[Int] = {
    try {
      val anInteger = Integer.parseInt(stringValue.trim)
      Some(anInteger)
    } catch {
      case e: Exception => None
    }
  }

  def toLong(stringValue: String): Option[Long] = {
    try {
      val aLong = stringValue.toLong
      return Some(aLong)
    } catch {
      case e: Exception => None
    }
  }
}
