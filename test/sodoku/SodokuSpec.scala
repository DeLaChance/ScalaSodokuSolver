package sodoku

import main.scala.sodoku.domain.Sodoku
import org.scalatest._

class SodokuSpec extends FunSuite with DiagrammedAssertions {

  test("Test that empty sodoku is valid") {
    // Given
    val sodoku = new Sodoku(9)
    print(sodoku.toString())

    // When
    val isValid = sodoku.isValid()

    // Then
    assert(isValid)
  }

  test("Test that full sodoku is valid") {
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
    val sodoku = new Sodoku(input)
    print(sodoku.toString())

    // When
    val isValid = sodoku.isValid()

    // Then
    assert(isValid)
  }

  test("Test that double digit on single row sodoku is invalid") {
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
    val sodoku = new Sodoku(input)
    print(sodoku.toString())

    // When
    val isValid = sodoku.isValid()

    // Then
    assert(!isValid)
  }

  test("Test that double digit on single column sodoku is invalid") {
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
    val sodoku = new Sodoku(input)
    print(sodoku.toString())

    // When
    val isValid = sodoku.isValid()

    // Then
    assert(!isValid)
  }

  test("Test that double digit in one box of a sodoku is invalid") {
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
    val sodoku = new Sodoku(input)
    print(sodoku.toString())

    // When
    val isValid = sodoku.isValid()

    // Then
    assert(!isValid)
  }


}

