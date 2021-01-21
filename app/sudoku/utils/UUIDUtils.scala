package sudoku.utils

import java.util.UUID

object UUIDUtils {

  def mapOrError(stringValue: String): UUID = {
    try {
      return UUID.fromString(stringValue)
    } catch {
      case _ => throw new IllegalArgumentException(s"String '${stringValue}' is not a UUID.")
    }
  }
}
