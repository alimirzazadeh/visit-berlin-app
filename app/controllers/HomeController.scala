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
  def beforelogin = Action {
    Ok(views.html.login(null))
  }

  // Home Page
  def index = Action {
    Ok(views.html.index(null))
  }

  def register = Action {
    Ok(views.html.register(assetsFinder))
  }

  def login = Action {
    Ok(views.html.login(null))
  }

  def logout = Action {
    Ok(views.html.index(null))
  }

  def place = Action {
    Ok(views.html.placepage("Account", assetsFinder))
  }

  def after = Action { implicit request =>
    val newProfile = Profile(request.body.asFormUrlEncoded.get("firstname").head.toUpperCase,
      request.body.asFormUrlEncoded.get("lastname").head.toUpperCase,
      request.body.asFormUrlEncoded.get("birthyear").head.toInt,
      request.body.asFormUrlEncoded.get("hometown").head.toUpperCase,
      request.body.asFormUrlEncoded.get("interests").head)
    val newAccount = Account(request.body.asFormUrlEncoded.get("email").head,
      Account.hashPasswordPlusSalt(request.body.asFormUrlEncoded.get("password").head),
      newProfile,
      // Change this to check user input match to the admin password's salt-free hash
      admin=true)
    val am = new AccountManager
    am.writeToCSV(am.addAccount(newAccount))
    Ok(views.html.index(newAccount.profile.firstName + " " + newAccount.profile.lastName))
  }

  def afterlogin = Action { implicit request =>
    val email = request.body.asFormUrlEncoded.get("email").head
    val password = request.body.asFormUrlEncoded.get("password").head
    val am = new AccountManager
    val accountTest = am.verifyLogin(email, password)
    accountTest match {
      case None => Ok(views.html.login("INCORRECT PASSWORD"))
      case Some(userAccount) => Ok(views.html.index(userAccount.profile.firstName + " " + userAccount.profile.lastName))
    }
  }

  val userForm = Form(
    mapping(
      "firstname" -> text,
      "lastname"  -> text,
      "email" -> text,
      "hometown" -> text,
      "interests" -> text,
      "password" -> text,
      "birthyear" -> number
    )(UserData.apply)(UserData.unapply)
  )
//  def formStuff = Action {
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