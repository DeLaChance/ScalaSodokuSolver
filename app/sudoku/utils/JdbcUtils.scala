package sudoku.utils

import java.sql.ResultSet

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

object JdbcUtils {

  def toRowsList(resultSet: ResultSet): List[Map[String, Object]] = {

    val maps: ListBuffer[Map[String, Object]] = ListBuffer.empty[Map[String, Object]]
    while(resultSet.next()) {
      val map: mutable.Map[String, Object] = mutable.HashMap()
      val metaData = resultSet.getMetaData()
      Range.apply(1, metaData.getColumnCount()+1)
        .map(i => metaData.getColumnName(i))
        .foreach(columnName => {
          val columnValue = resultSet.getObject(columnName)
          map(columnName) = columnValue
        })
        maps += map.toMap[String, Object]
    }

    return maps.toList
  }

  def extractStringOrError(map: Map[String, Object], key: String): String = {
    val optionalString = extractString(map, key)
    if (optionalString.isEmpty) {
      throw new IllegalStateException(s"JDBC result should include value ${key}")
    } else {
      return optionalString.get
    }
  }

  def extractLongOrError(map: Map[String, Object], key: String): Long = {
    val optionalLong = extractLong(map, key)
    if (optionalLong.isEmpty) {
      throw new IllegalStateException(s"JDBC result should include value ${key}")
    } else {
      return optionalLong.get
    }
  }

  def extractLong(map: Map[String, Object], key: String): Option[Long] = {
    return extractString(map, key).flatMap(stringValue => IntegerUtils.toLong(stringValue))
  }

  def extractString(map: Map[String, Object], key: String): Option[String] = {
    return extractObject(map, key).map(objectValue => objectValue.toString())
  }

  private def extractObject(map: Map[String, Object], key: String): Option[Object] = {

    var value: Option[Object] = None
    if (map.contains(key)) {
      value = map.get(key)
    }

    return value
  }

}
