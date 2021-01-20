package sudoku.config

import play.api.http.HttpErrorHandler
import play.api.mvc._
import play.api.mvc.Results._

import scala.concurrent._
import javax.inject.Singleton
import play.api.Logger

@Singleton
class CustomHttpErrorHandler extends HttpErrorHandler {

  private val logger = Logger(getClass)

  def onClientError(request: RequestHeader, statusCode: Int, message: String): Future[Result] = {
    Future.successful(
      Status(statusCode)("A client error occurred: " + message)
    )
  }

  def onServerError(request: RequestHeader, exception: Throwable): Future[Result] = {

    logger.error(s"A server error occurred: ", exception)
    Future.successful(
      InternalServerError("A server error occurred: " + exception.getMessage)
    )
  }
}