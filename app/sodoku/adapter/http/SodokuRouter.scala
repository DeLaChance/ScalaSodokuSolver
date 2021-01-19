package sodoku.adapter.http

import javax.inject.Inject
import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import play.api.routing.sird._

class SodokuRouter @Inject()(controller: SodokuController) extends SimpleRouter {

  val prefix = "api/sodokus"

  override def routes: Routes = {
    case GET(p"/") => controller.helloWorld
    case GET(p"/new") => controller.fetchEmptySodoku
  }
}
