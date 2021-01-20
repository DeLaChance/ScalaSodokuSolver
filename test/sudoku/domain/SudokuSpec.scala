package sudoku.domain

import main.scala.sodoku.domain.Sudoku
import org.scalatest._
import play.api.libs.json.{JsObject, JsString, JsValue}

class SudokuSpec extends FunSuite with DiagrammedAssertions {

  test("Test that empty sudoku is valid") {
    // Given
    val sudoku = new Sudoku(9)
    print(sudoku.toString())

    // When
    val isValid = sudoku.isValid()

    // Then
    assert(isValid)
  }

  test("Test that full sudoku is valid") {
    // Given
    val input = "" +
      "1,2,3,4,5,6,7,8,9\n" +
      "4,5,6,7,8,9,1,2,3\n" +
      "7,8,9,1,2,3,4,5,6\n" +
      "2,3,4,5,6,7,8,9,1\n" +
      "5,6,7,8,9,1,2,3,4\n" +
      "8,9,1,2,3,4,5,6,7\n" +
      "3,4,5,6,7,8,9,1,2\n" +
      "6,7,8,9,1,2,3,4,5\n" +
      "9,1,2,3,4,5,6,7,8\n"
    val sudoku = new Sudoku(input)
    print(sudoku.toString())

    // When
    val isValid = sudoku.isValid()

    // Then
    assert(isValid)
  }

  test("Test that double digit on single row sudoku is invalid") {
    // Given
    val input = "" +
      "1,2,3,4,5,6,7,8,1\n" +
      "4,5,6,7,8,9,1,2,3\n" +
      "7,8,9,1,2,3,4,5,6\n" +
      "2,3,4,5,6,7,8,9,*\n" +
      "5,6,7,8,9,1,2,3,4\n" +
      "8,9,1,2,3,4,5,6,7\n" +
      "3,4,5,6,7,8,9,1,2\n" +
      "6,7,8,9,1,2,3,4,5\n" +
      "9,1,2,3,4,5,6,7,8\n"
    val sudoku = new Sudoku(input)
    print(sudoku.toString())

    // When
    val isValid = sudoku.isValid()

    // Then
    assert(!isValid)
  }

  test("Test that double digit on single column sudoku is invalid") {
    // Given
    val input = "" +
      "1,2,3,4,5,6,7,8,9\n" +
      "4,5,6,7,8,9,1,2,3\n" +
      "7,8,9,1,2,3,4,5,6\n" +
      "2,3,4,5,6,7,8,9,1\n" +
      "5,6,7,8,9,1,2,3,4\n" +
      "8,9,1,2,3,4,5,6,7\n" +
      "3,4,5,6,7,8,9,1,2\n" +
      "1,7,8,9,*,2,3,4,5\n" +
      "9,1,2,3,4,5,6,7,8\n"
    val sudoku = new Sudoku(input)
    print(sudoku.toString())

    // When
    val isValid = sudoku.isValid()

    // Then
    assert(!isValid)
  }

  test("Test that double digit in one box of a sudoku is invalid") {
    // Given
    val input = "" +
      "1,2,3,4,5,6,7,8,1\n" +
      "4,5,6,7,8,9,1,2,3\n" +
      "7,8,2,1,*,3,4,5,6\n" +
      "2,3,4,5,6,7,8,9,*\n" +
      "5,6,7,8,9,1,2,3,4\n" +
      "8,9,1,2,3,4,5,6,7\n" +
      "3,4,5,6,7,8,9,1,2\n" +
      "6,7,8,9,1,2,3,4,5\n" +
      "9,1,*,3,4,5,6,7,8\n"
    val sudoku = new Sudoku(input)
    print(sudoku.toString())

    // When
    val isValid = sudoku.isValid()

    // Then
    assert(!isValid)
  }

  test("Test that box is valid") {
    // Given
    val input = "" +
      "1,2,3,-,-,-,-,-,-\n" +
      "4,5,6,-,-,-,-,-,-\n" +
      "7,8,9,-,-,-,-,-,-\n" +
      "-,-,-,-,-,-,-,-,-\n" +
      "-,-,-,-,-,-,-,-,-\n" +
      "-,-,-,-,-,-,-,-,-\n" +
      "-,-,-,-,-,-,1,2,3\n" +
      "-,-,-,-,-,-,4,5,6\n" +
      "-,-,-,-,-,-,7,8,9"
    val sudoku = new Sudoku(input)
    print(sudoku.toString())

    // When
    val isValid = sudoku.isValid()

    // Then
    assert(isValid)
  }

  test("Test that box is invalid") {
    // Given
    val input = "" +
      "1,2,3,-,-,-,-,-,-\n" +
      "4,5,6,-,-,-,-,-,-\n" +
      "7,8,9,-,-,-,-,-,-\n" +
      "-,-,-,1,2,3,-,-,-\n" +
      "-,-,-,4,5,5,-,-,-\n" +
      "-,-,-,6,7,8,-,-,-\n" +
      "-,-,-,-,-,-,1,2,3\n" +
      "-,-,-,-,-,-,4,5,6\n" +
      "-,-,-,-,-,-,7,8,9"
    val sudoku = new Sudoku(input)
    print(sudoku.toString())

    // When
    val isValid = sudoku.isValid()

    // Then
    assert(!isValid)
  }

  test("Test that sudoku is deserialized correctly") {
    // Given
    val input = "" +
      "1,2,3,4,5,6,7,8,1\n" +
      "4,5,6,7,8,9,1,2,3\n" +
      "7,8,2,1,-,3,4,5,6\n" +
      "2,3,4,5,6,7,8,9,-\n" +
      "5,6,7,8,9,1,2,3,4\n" +
      "8,9,1,2,3,4,5,6,7\n" +
      "3,4,5,6,7,8,9,1,2\n" +
      "6,7,8,9,1,2,3,4,5\n" +
      "9,1,-,3,4,5,6,7,8"
    val sudokuJson: JsValue = JsObject(Seq("contents" -> JsString(input)))
    val sudoku = Sudoku.fromJson(sudokuJson)

    // When
    val deserializedSudoku = sudoku.toString()

    // Then
    println(input)
    println("---")
    println(deserializedSudoku)

    assert(input == deserializedSudoku)
  }

  test("Test that box candidates are as expected") {
    // Given
    val input = "" +
      "1,2,3,-,-,-,-,-,-\n" +
      "-,-,-,-,-,-,5,-,-\n" +
      "-,-,-,-,-,-,-,-,-\n" +
      "-,-,-,-,-,-,-,-,-\n" +
      "-,-,-,-,-,-,-,-,-\n" +
      "-,-,-,-,-,-,-,-,-\n" +
      "4,-,-,-,-,-,1,2,3\n" +
      "-,-,-,-,-,-,4,5,6\n" +
      "-,-,-,-,-,-,7,8,9"
    val sudoku = new Sudoku(input)
    print(sudoku.toString())

    // When
    val candidates = sudoku.fetchCandidates(9)

    // Then
    assert(candidates.equals(List(6,7,8,9)))
  }

  test("Test that box candidates are as expected 2") {
    // Given
    val input = "" +
      "1,2,3,-,-,-,-,-,-\n" +
      "-,-,-,-,-,-,5,-,-\n" +
      "-,-,-,-,-,-,-,-,-\n" +
      "-,-,-,-,-,-,-,-,-\n" +
      "-,-,-,-,-,-,-,-,-\n" +
      "-,-,-,-,-,-,-,-,-\n" +
      "4,-,-,-,-,-,1,2,3\n" +
      "-,-,-,-,-,-,4,5,6\n" +
      "-,-,-,-,-,-,7,8,9"
    val sudoku = new Sudoku(input)
    print(sudoku.toString())

    // When
    val candidates = sudoku.fetchCandidates(4)

    // Then
    assert(candidates.equals(List(4,5,6,7,8,9)))
  }

  test("Test that box candidates are as expected 3") {
    // Given
    val input =
      "1,4,5,6,2,3,7,8,9\n2,8,6,7,9,5,-,-,-\n3,-,-,-,-,-,-,-,-\n" +
      "4,-,-,-,-,-,-,-,-\n5,-,-,-,-,-,-,-,-\n6,-,-,-,-,-,-,-,-\n" +
      "7,-,-,-,-,-,-,-,-\n8,-,-,-,-,-,-,-,-\n9,-,-,-,-,-,-,-,-"

    val sudoku = new Sudoku(input)
    print(sudoku.toString())

    // When
    val candidates = sudoku.fetchCandidates(15)

    // Then
    assert(candidates.equals(List(1,3,4)))
  }
}

