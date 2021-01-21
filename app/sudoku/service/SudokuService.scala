package sudoku.service

import javax.inject.{Inject, Singleton}
import main.scala.sodoku.domain.Sudoku
import play.api.Logger
import play.api.libs.json.JsValue
import sudoku.repository.SudokuRepository
import sudoku.utils.UUIDUtils

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class SudokuService @Inject()(sudokuRepository: SudokuRepository)(implicit ec: ExecutionContext) {

  private val logger = Logger(getClass)

  def fetchAll(): Future[List[Sudoku]] = sudokuRepository.fetchAll()

  def fetchById(id: String) = sudokuRepository.fetchById(UUIDUtils.mapOrError(id))

  def createNew(): Future[Sudoku] = {
    val sudoku: Sudoku = Sudoku.emptySodoku()
    return sudokuRepository.insert(sudoku)
  }

  def uploadJson(json: JsValue): Future[Sudoku] = {
    val sudoku: Sudoku = Sudoku.fromJson(json)
    return sudokuRepository.insert(sudoku)
  }

  def save(sudoku: Sudoku): Future[Sudoku] = {
    fetchById(sudoku.getId().toString)
      .flatMap(optionalSudoku => {
        if (optionalSudoku.isEmpty) {
          sudokuRepository.insert(sudoku)
        } else {
          sudokuRepository.update(sudoku)
        }
      })
  }

  def deleteById(id: String): Future[Option[Sudoku]] = {
    return sudokuRepository.fetchById(UUIDUtils.mapOrError(id))
      .flatMap(optionalSudoku => {
        if (optionalSudoku.isDefined) {
          sudokuRepository.delete(optionalSudoku.get)
            .map(sudoku => Some(sudoku))
        } else {
          Future.successful(None)
        }
      })
  }

  def solveSudoku(id: String): Future[Option[Sudoku]] = {
    return sudokuRepository.fetchById(UUIDUtils.mapOrError(id))
      .flatMap(optionalSudoku => {
        if (optionalSudoku.isDefined) {
          val sudoku = optionalSudoku.get

          logger.info(s"Start solving sudoku ${sudoku.getId()}")
          solveSudoku(sudoku, sudoku.fetchFirstOpenCell())

          if (sudoku.isComplete()) {
            logger.info(s"Found sudoku ${sudoku.getId()} solution: ${sudoku.toString()}")
          }

          save(sudoku).map(sudoku => Some(sudoku))
        } else {
          Future.successful(None)
        }
      })
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
