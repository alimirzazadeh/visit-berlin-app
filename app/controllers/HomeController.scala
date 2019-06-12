package controllers

import javax.inject._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models.UserData


class HomeController @Inject()(cc: ControllerComponents)(implicit assetsFinder: AssetsFinder) extends AbstractController(cc) {

  def hello = Action {
    Ok(views.html.hello())
  }
  def before = Action {
    Ok(views.html.hello())
  }


  def after = Action { implicit request =>
    print("HIIII")
    Ok(views.html.after(request.body.asFormUrlEncoded.toString))
  }

  val userForm = Form(
    mapping(
      "firstname" -> text,
      "lastname"  -> text,
      "username" -> text,
      "email" -> text
    )(UserData.apply)(UserData.unapply)
  )
//  def formstuff = Action {
//    Ok(views.html.)
//  }
}

//case class UserData(name: String, age: Int) {
//  val userForm = Form(
//    mapping(
//      "name" -> text,
//      "age"  -> number
//    )(UserData.apply)(UserData.unapply)
//  )
//  val anyData  = Map("name" -> "bob", "age" -> "21")
//  val userData = userForm.bindFromRequest.get
//}