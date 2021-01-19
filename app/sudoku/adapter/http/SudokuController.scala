package sudoku.adapter.http

import javax.inject.Inject
import main.scala.sodoku.domain.Sudoku
import play.api.Logger
import play.api.mvc.{AbstractController, ControllerComponents}
import sudoku.service.SudokuService

import scala.concurrent.ExecutionContext

class SudokuController @Inject()(
    components: ControllerComponents,
    sudokuService: SudokuService
  )
  (implicit ec: ExecutionContext) extends AbstractController(components: ControllerComponents) {

  private val logger = Logger(getClass)

  def fetchAll = Action.async { implicit httpRequest =>
    logger.info("Fetching sudoku's")
    sudokuService.fetchAll()
      .map(sudokus => Ok(Sudoku.toJson(sudokus)))
  }

  def fetchById(id: String) = Action.async { implicit httpRequest =>
    logger.info(s"Fetching sudoku ${id}")
    sudokuService.fetchById(id)
      .map(optionalSudoku => mapToHttpResponse(optionalSudoku))
  }

  def createNew = Action.async { implicit httpRequest =>
    logger.info("Creating new empty sodoku")
    sudokuService.createNew()
      .map(sodoku => Ok(Sudoku.toJson(sodoku)))
  }

  def mapToHttpResponse(optionalValue: Option[Sudoku]) = {
    if (optionalValue.isEmpty) {
      NotFound("Not found")
    } else {
      Ok(Sudoku.toJson(optionalValue.get))
    }
  }
}
