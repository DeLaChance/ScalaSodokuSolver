package main.scala.sodoku.domain

import java.util.UUID

import play.api.libs.json.{JsArray, JsValue, Json, Writes}
import sudoku.utils.IntegerUtils

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer


class Sudoku(
  private var id: UUID = UUID.randomUUID(),
  private var contents: Array[Option[Int]]
) {

  def this(dimension: Int) {
    this(contents = Array.fill[Option[Int]](dimension * dimension)(None))
  }

  def this(input: String) {
    this(contents = input.replaceAll("\"", "")
      .replaceAll("[^\\,\\n\\ [0-9]+\\-\\*\\\\n]","")
      .split("(\\,|\\n|\\ |\\\\n)")
      .filter(aString => !aString.isBlank())
      .map(aString => IntegerUtils.toInt(aString)))
  }

  private val dimension: Int = Math.round(Math.sqrt(contents.length)).toInt
  private val boxDimension = Math.round(Math.sqrt(dimension)).toInt

  if (contents.size != dimension*dimension || dimension != boxDimension*boxDimension) {
    throw new IllegalArgumentException(s"Contents size is ${contents.size}, but needs to be a double square, " +
      s"for example 81 or 16")
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

  def isComplete(): Boolean = fetchFirstOpenCell().isEmpty

  override def toString: String = {

    val stringBuffer = new StringBuffer()
    for (i <- 0 until dimension) {
      for (j <- 0 until dimension) {
        val cellNumber = i * dimension + j
        val cellValue = getCellValue(cellNumber)
        cellValue match {
          case Some(intCellValue) => stringBuffer.append(s"${intCellValue}")
          case None => stringBuffer.append(s"${Sudoku.EMPTY_CELL_VALUE}")
        }

        if (j < dimension-1) {
          stringBuffer.append(",")
        } else {
          if (i < dimension-1) {
            stringBuffer.append("\n")
          }
        }
      }
    }

    return stringBuffer.toString
  }

  def getCellValue(cellNumber: Int): Option[Int] = {
    assertWithinBounds(cellNumber)
    return contents(cellNumber)
  }

  def emptyCellValue(cellNumber: Int): Unit = {
    setCellValue(cellNumber, None)
  }

  def setCellValue(cellNumber: Int, cellValue: Int): Unit = {
    setCellValue(cellNumber, Some(cellValue))
  }

  def getId(): UUID = id

  def fetchFirstOpenCell(): Option[Int] = {
    for (cellNumber <- 0 until dimension*dimension) {
      val cellValue = getCellValue(cellNumber)
      if (cellValue.isEmpty) {
        return Some(cellNumber)
      }
    }

    return None
  }

  def fetchCandidates(firstOpenCell: Int): List[Int] = {

    val rowRange = determineRowRange(firstOpenCell / dimension)
    val columnRange = determineColumnRange(firstOpenCell % dimension)
    val boxNumber = ((firstOpenCell / (boxDimension * dimension)) * boxDimension) + (firstOpenCell % dimension / boxDimension)
    val boxRange = determineBoxRange(boxNumber)

    val allRanges = Set.concat(rowRange, columnRange, boxRange)

    val candidates: mutable.Set[Int] = mutable.HashSet.range(1, dimension+1)
    allRanges.map(cellNumber => getCellValue(cellNumber)).foreach(cellValue => {
      if (cellValue.isDefined) {
        candidates.remove(cellValue.get)
      }
    })

    return candidates.toList
  }

  private def setCellValue(cellNumber: Int, cellValue: Option[Int]): Unit = {
    assertWithinBounds(cellNumber)
    if (cellValue.isDefined) {
      assertValueAllowed(cellValue.get)
    }

    contents(cellNumber) = cellValue
  }

  private def assertWithinBounds(cellNumber: Int) = if (cellNumber < 0 || cellNumber >= contents.size) {
    throw new IllegalArgumentException(s"${cellNumber} is out of bounds")
  }

  private def assertValueAllowed(cellValue: Int) = if (cellValue == None || cellValue < 0 ||
    cellValue > dimension) {

    throw new IllegalArgumentException(s"${cellValue} is out of bounds")
  }

  private def isRowValid(rowNumber: Int): Boolean = {
    val rowRange = determineRowRange(rowNumber)
    return isRangeValid(Set[Int](), rowRange)
  }

  private def determineRowRange(rowNumber: Int): List[Int] = {
    val begin = rowNumber * dimension
    val end = (rowNumber+1) * dimension
    val rowRange: List[Int] = List.range(begin, end)
    return rowRange
  }

  private def isColumnValid(columnNumber: Int): Boolean = {
    val columnRange: List[Int] = determineColumnRange(columnNumber)
    return isRangeValid(Set[Int](), columnRange)
  }

  def determineColumnRange(columnNumber: Int): List[Int] = {
    val begin = columnNumber
    val end = columnNumber + dimension * (dimension-1)
    val step = dimension
    val columnRange: List[Int] = List.range(begin, end, step)
    return columnRange
  }

  private def isBoxValid(boxNumber: Int): Boolean = {
    val boxRange: List[Int] = determineBoxRange(boxNumber)
    return isRangeValid(Set[Int](), boxRange.toList)
  }

  private def determineBoxRange(boxNumber: Int): List[Int] = {
    val begin = boxNumber * boxDimension + (boxNumber / 3) * (dimension * 2)
    val boxRange: ArrayBuffer[Int] = ArrayBuffer()

    for (i <- 0 until boxDimension) {
      for (j <- 0 until boxDimension) {
        boxRange += begin + (i * dimension) + j
      }
    }

    return boxRange.toList
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

  val EMPTY_CELL_VALUE: String = "-"

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

  def fromJson(json: JsValue): Sudoku = {
    val contentsString: String = json("contents").toString()
    return new Sudoku(input = contentsString)
  }

  implicit val implicitWrites = new Writes[Sudoku] {
    def writes(sodoku: Sudoku): JsValue = toJson(sodoku)
  }

}

