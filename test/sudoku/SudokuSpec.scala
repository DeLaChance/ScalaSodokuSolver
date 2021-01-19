package sudoku

import main.scala.sodoku.domain.Sudoku
import org.scalatest._

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


}

