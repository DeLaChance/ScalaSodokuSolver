package main.scala.sodoku.domain

import java.util.UUID

import play.api.libs.json.{JsArray, JsValue, Json, Writes}
import sudoku.utils.IntegerUtils

import scala.collection.mutable.ArrayBuffer


class Sudoku(
  private var id: UUID = UUID.randomUUID(),
  private var contents: Array[Option[Int]]
) {

  private val dimension: Int = Math.round(Math.sqrt(contents.length)).toInt
  private val boxDimension = Math.round(Math.sqrt(dimension)).toInt

  def this(dimension: Int) {
    this(contents = Array.fill[Option[Int]](dimension * dimension)(None))
  }

  def this(input: String) {
    this(contents = input.split("(\\,|\\n)").map(aString => IntegerUtils.toInt(aString)))
  }

  if (contents.size != dimension*dimension || dimension != boxDimension*boxDimension) {
    throw new IllegalArgumentException("Contents size needs to be a double square, e.g. 81 or 16")
  }

  def isValid(): Boolean = {
    var allRowsValid = true
    var allColumnsValid = true
    var allBoxesValid = true

    for (rowNumber <- 0 until dimension) {
      allRowsValid &= isRowValid(rowNumber)
      allColumnsValid &= isColumnValid(rowNumber)
      allBoxesValid &= isBoxValid(rowNumber)
    }

    return allRowsValid && allColumnsValid && allBoxesValid
  }

  override def toString: String = {

    val stringBuffer = new StringBuffer()
    for (i <- 0 until dimension) {
      for (j <- 0 until dimension) {
        val cellNumber = i * dimension + j
        val cellValue = getCellValue(cellNumber)
        cellValue match {
          case Some(intCellValue) => stringBuffer.append(s"${intCellValue},")
          case None => stringBuffer.append(s"-,")
        }
      }
      stringBuffer.append("\n")
    }

    return stringBuffer.toString
  }

  def getCellValue(cellNumber: Int): Option[Int] = {
    assertWithinBounds(cellNumber)
    return contents(cellNumber)
  }

  def setCellValue(cellNumber: Int, cellValue: Int): Unit = {
    assertWithinBounds(cellNumber)
    assertValueAllowed(cellValue)
    contents(cellNumber) = Some(cellValue)
  }

  def getId(): UUID = id

  private def assertWithinBounds(cellNumber: Int) = if (cellNumber < 0 || cellNumber >= contents.size) {
    throw new IllegalArgumentException(s"${cellNumber} is out of bounds")
  }

  private def assertValueAllowed(cellValue: Int) = if (cellValue == None || cellValue < 0 ||
    cellValue > dimension) {

    throw new IllegalArgumentException(s"${cellValue} is out of bounds")
  }

  private def isRowValid(rowNumber: Int): Boolean = {
    val begin = rowNumber * dimension
    val end = (rowNumber+1) * dimension
    val rowRange: List[Int] = List.range(begin, end)

    return isRangeValid(Set[Int](), rowRange)
  }

  private def isColumnValid(columnNumber: Int): Boolean = {
    val begin = columnNumber
    val end = columnNumber + dimension * (dimension-1)
    val step = dimension
    val columnRange: List[Int] = List.range(begin, end, step)

    return isRangeValid(Set[Int](), columnRange)
  }

  private def isBoxValid(boxNumber: Int): Boolean = {
    val begin = boxNumber * boxDimension + (boxNumber / 3) * (dimension * 2)
    val boxRange: ArrayBuffer[Int] = ArrayBuffer()

    for (i <- 0 until boxDimension) {
      for (j <- 0 until boxDimension) {
        boxRange += begin + (i * dimension) + j
      }
    }

    return isRangeValid(Set[Int](), boxRange.toList)
  }

  private def isRangeValid(encounteredElements: Set[Int], range: List[Int]): Boolean = {

    var isValid = true
    if (range.size > 0) {
      val cellNumber: Int = range(0)
      val cellValue = getCellValue(cellNumber)
      if (cellValue.isEmpty) {
        isValid &= isRangeValid(encounteredElements, range.slice(1, range.size))
      } else {
        if (encounteredElements.contains(cellValue.get)) {
          isValid = false
        } else {
          isValid &= isRangeValid(encounteredElements + cellValue.get, range.slice(1, range.size))
        }
      }
    }

    return isValid
  }
}

object Sudoku {

  def emptySodoku(): Sudoku = new Sudoku(9)

  def toJson(sudokus: List[Sudoku]): JsValue = {
    Json.obj(
      "count" -> sudokus.length,
      "items" -> JsArray(
        sudokus.map{ sudoku => toJson(sudoku) }
      )
    )
  }

  def toJson(sodoku: Sudoku): JsValue = {
    Json.obj(
      "id" -> sodoku.id.toString,
      "hasError" -> !sodoku.isValid,
      "contents" -> sodoku.toString
    )
  }

  implicit val implicitWrites = new Writes[Sudoku] {
    def writes(sodoku: Sudoku): JsValue = toJson(sodoku)
  }

}

