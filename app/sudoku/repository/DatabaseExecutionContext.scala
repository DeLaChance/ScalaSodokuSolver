package sudoku.repository

import javax.inject._

import akka.actor.ActorSystem
import play.api.libs.concurrent.CustomExecutionContext

class DatabaseExecutionContext @Inject()(system: ActorSystem) extends CustomExecutionContext(system, "database.dispatcher") {

}
