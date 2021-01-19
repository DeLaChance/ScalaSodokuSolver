package sodoku.adapter.http

import javax.inject.Inject
import main.scala.sodoku.domain.Sodoku
import play.api.Logger
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.{ExecutionContext, Future}

class SodokuController @Inject() (components: ControllerComponents)
  (implicit ec: ExecutionContext) extends AbstractController(components: ControllerComponents) {

  private val logger = Logger(getClass)

  def helloWorld = Action.async { implicit httpRequest =>
    logger.info("Hello world")
    val response = Future.successful("Hello world")
      .map(message => Ok(message))
    response
  }

  def fetchEmptySodoku = Action.async { implicit httpRequest =>
    logger.info("Fetching empty sodoku")
    Future.successful(Sodoku.emptySodoku())
      .map(sodoku => Ok(Sodoku.toJson(sodoku)))
  }
}
