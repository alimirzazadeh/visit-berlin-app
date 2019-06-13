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

  // Home Page
  def index = Action {
    Ok(views.html.index(assetsFinder))
  }

  def register = Action {
    Ok(views.html.register(assetsFinder))
  }

  def after = Action { implicit request =>
    val newProfile = Profile(request.body.asFormUrlEncoded.get("firstName").head, request.body.asFormUrlEncoded.get("lastName").head,
      request.body.asFormUrlEncoded.get("birthYear").head.toInt, request.body.asFormUrlEncoded.get("hometown").head,
      request.body.asFormUrlEncoded.get("interests").head)
    val newAccount = new Account(request.body.asFormUrlEncoded.get("email").head, request.body.asFormUrlEncoded.get("password").head, Account.generateSalt(), newProfile, false)
    //AccountManager.addAccount(newaccount);
    val am = new AccountManager
    am.writeToCSV(am.addAccount(newAccount))
    //
    Ok(views.html.after(newAccount))
  }

  val userForm = Form(
    mapping(
      "firstName" -> text,
      "lastName"  -> text,
      "email" -> text,
      "hometown" -> text,
      "interests" -> text,
      "password" -> text,
      "birthYear" -> number
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