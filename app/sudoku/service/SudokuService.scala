package sudoku.service

import java.util.UUID

import javax.inject.Singleton
import main.scala.sodoku.domain.Sudoku
import play.api.libs.json.JsValue

import scala.collection.mutable
import scala.concurrent.Future

@Singleton
class SudokuService {

  val sudokuRepo: mutable.Map[UUID, Sudoku] = new mutable.HashMap[UUID, Sudoku]()

  def fetchAll(): Future[List[Sudoku]] = {
    Future.successful(sudokuRepo.values.toList)
  }

  def fetchById(id: String) = {
    val sudoku: Option[Sudoku] = findById(id)
    Future.successful(sudoku)
  }

  def createNew(): Future[Sudoku] = {
    val sudoku: Sudoku = Sudoku.emptySodoku()
    return saveToRepo(sudoku)
  }

  def uploadJson(json: JsValue): Future[Sudoku] = {
    val sudoku: Sudoku = Sudoku.fromJson(json)
    saveToRepo(sudoku)
  }

  def save(sudoku: Sudoku): Future[Sudoku] = {
    saveToRepo(sudoku)
  }

  def deleteById(id: String): Future[Option[Sudoku]] = {
    val optionalSudoku: Option[Sudoku] = findById(id)

    if (optionalSudoku.isDefined) {
        sudokuRepo.remove(optionalSudoku.get.getId())
    }

    return Future.successful(optionalSudoku)
  }

  def solveSudoku(id: String): Future[Option[Sudoku]] = {
    val optionalSudoku: Option[Sudoku] = findById(id)

    if (optionalSudoku.isDefined) {
      val sudoku = optionalSudoku.get

      if (sudoku.isValid()) {
        solveSudoku(sudoku, sudoku.fetchFirstOpenCell())
      }
    }

    return Future.successful(optionalSudoku)
  }

  private def saveToRepo(sudoku: Sudoku): Future[Sudoku] = {
    sudokuRepo.put(sudoku.getId(), sudoku)
    return Future.successful(sudoku)
  }

  private def findById(id: String): Option[Sudoku] = {
    try {
      return Some(sudokuRepo(UUID.fromString(id)))
    } catch {
      case _ => return None
    }
  }

  private def solveSudoku(sudoku: Sudoku, firstOpenCell: Option[Int]): Sudoku = {

    if (firstOpenCell.isDefined) {
      val firstOpenCellNumber: Int = firstOpenCell.get

      val candidates: List[Int] = sudoku.fetchCandidates(firstOpenCellNumber)
      for (candidate <- candidates) {
        sudoku.setCellValue(firstOpenCellNumber, candidate)

        if (sudoku.isValid()) {
          solveSudoku(sudoku, sudoku.fetchFirstOpenCell())

          if (sudoku.isComplete()) {
            return sudoku
          }
        }
      }

      sudoku.emptyCellValue(firstOpenCellNumber)
    }

    return sudoku
  }

}
