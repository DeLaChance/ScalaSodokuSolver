package sudoku.adapter.http

import javax.inject.Inject
import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import play.api.routing.sird._

class SudokuRouter @Inject()(controller: SudokuController) extends SimpleRouter {

  val prefix = "api/sudoku"

  override def routes: Routes = {
    case GET(p"/") => controller.fetchAll
    case GET(p"/new") => controller.createNew
    case GET(p"/$id") => controller.fetchById(id: String)
  }
}
