package controllers

import javax.inject._
import play.api.mvc._

class HomeController @Inject()(cc: ControllerComponents)(implicit assetsFinder: AssetsFinder) extends AbstractController(cc) {
  def hello = Action {
    Ok(views.html.hello())
  }
}