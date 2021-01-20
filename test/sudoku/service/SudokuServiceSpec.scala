package sudoku.service

import main.scala.sodoku.domain.Sudoku
import org.scalatest._

class SudokuServiceSpec extends FunSuite with BeforeAndAfterEach {

  private var instanceToBeTested: SudokuService = null

  override def beforeEach() {
    instanceToBeTested = new SudokuService()
  }

  test("Test that a valid sudoku solution is provided") {
    // Given
    val input = "" +
      "1,-,-,-,-,-,-,-,-\n" +
      "2,-,-,-,-,-,-,-,-\n" +
      "3,-,-,-,-,-,-,-,-\n" +
      "4,-,-,-,-,-,-,-,-\n" +
      "5,-,-,-,-,-,-,-,-\n" +
      "6,-,-,-,-,-,-,-,-\n" +
      "7,-,-,-,-,-,-,-,-\n" +
      "8,-,-,-,-,-,-,-,-\n" +
      "9,-,-,-,-,-,-,-,-"
    val sudoku = new Sudoku(input = input)
    instanceToBeTested.save(sudoku)

    // When
    instanceToBeTested.solveSudoku(sudoku.getId().toString())

    // Then
    println(sudoku)

    assert(sudoku.isValid())
    assert(sudoku.fetchFirstOpenCell().isEmpty)
  }

}

