package sudoku.repository

import java.sql.{ResultSet, SQLException}
import java.util.UUID

import javax.inject.{Inject, Singleton}
import main.scala.sodoku.domain.Sudoku
import play.api.db.Database
import sudoku.utils.JdbcUtils

import scala.concurrent.Future

@Singleton
class SudokuRepository @Inject()(db: Database)(implicit databaseExecutionContext: DatabaseExecutionContext) {

  def fetchAll(): Future[List[Sudoku]] = {
    db.withConnection { implicit connection =>
      val resultSet: ResultSet = connection.prepareStatement(SudokuRepository.fetchAllSql)
        .executeQuery()
      val maps = JdbcUtils.toRowsList(resultSet)
      val sudokus = maps.map(map => Sudoku.mapFromJdbc(map))
      Future.successful(sudokus)
    }
  }

  def fetchById(id: UUID): Future[Option[Sudoku]] = {
    db.withConnection { implicit connection =>
      val preparedStatement = connection.prepareStatement(SudokuRepository.fetchByIdSql)
      preparedStatement.setString(1, id.toString())

      val resultSet = preparedStatement.executeQuery()

      val singleResultOptional = JdbcUtils.toRowsList(resultSet).headOption
      val sudokuOptional = singleResultOptional.map(map => Sudoku.mapFromJdbc(map))

      Future.successful(sudokuOptional)
    }
  }

  def insert(sudoku: Sudoku): Future[Sudoku] = {
    db.withConnection { implicit connection =>
      val preparedStatement = connection.prepareStatement(SudokuRepository.insertSql)
      preparedStatement.setString(1, sudoku.getId().toString())
      preparedStatement.setString(2, sudoku.toString())
      preparedStatement.setLong(3,sudoku.getCreated())

      val successCount: Int = preparedStatement.executeUpdate()
      if (successCount == 1) {
        Future.successful(sudoku)
      } else {
        Future.failed(new SQLException("Insert failed"))
      }
    }
  }

  def update(sudoku: Sudoku): Future[Sudoku] = {
    db.withConnection { implicit connection =>
      val preparedStatement = connection.prepareStatement(SudokuRepository.updateSql)
      preparedStatement.setString(1, sudoku.toString())
      preparedStatement.setString(2, sudoku.getId().toString())

      val successCount: Int = preparedStatement.executeUpdate()
      if (successCount == 1) {
        Future.successful(sudoku)
      } else {
        Future.failed(new SQLException("Insert failed"))
      }
    }
  }

  def delete(sudoku: Sudoku): Future[Sudoku] = {
    db.withConnection { implicit connection =>
      val preparedStatement = connection.prepareStatement(SudokuRepository.deleteSql)
      preparedStatement.setString(1, sudoku.getId().toString())

      val successCount: Int = preparedStatement.executeUpdate()
      if (successCount == 1) {
        Future.successful(sudoku)
      } else {
        Future.failed(new SQLException("Insert failed"))
      }
    }
  }

}

object SudokuRepository {

  val fetchAllSql =
    """
      select s."contents", s."created", s."id"
      from sudoku.sudoku.sudoku s
       """

  val fetchByIdSql: String =
    """
      select s."contents", s."created", s."id"
      from sudoku.sudoku.sudoku s
      where s."id" = ?
      """

  val insertSql: String =
    """
      insert into sudoku.sudoku.sudoku ("id", "contents", "created")
      values (?, ?, ?)
      """

  val updateSql: String =
    """
      update sudoku.sudoku.sudoku
      set "contents" = ?
      where "id" = ?
      """

  val deleteSql: String =
    """
      delete from sudoku.sudoku.sudoku
      where "id" = ?
      """
}
