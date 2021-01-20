package sudoku.utils

object IntegerUtils {

  def toInt(s: String): Option[Int] = {
    try {
      val anInteger = Integer.parseInt(s.trim)
      Some(anInteger)
    } catch {
      case e: Exception => None
    }
  }

}
