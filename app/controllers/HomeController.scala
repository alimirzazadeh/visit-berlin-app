package controllers

import javax.inject._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models.UserData

class HomeController @Inject()(cc: ControllerComponents)(implicit assetsFinder: AssetsFinder)
  extends AbstractController(cc) {

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

  def manageAccount = Action {
    Ok(views.html.manageAccount(assetsFinder))
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
    val am = new AccountManager
    val newProfile = Profile(request.body.asFormUrlEncoded.get("firstname").head.toUpperCase,
      request.body.asFormUrlEncoded.get("lastname").head.toUpperCase,
      request.body.asFormUrlEncoded.get("birthyear").head.toInt,
      request.body.asFormUrlEncoded.get("hometown").head.toUpperCase,
      request.body.asFormUrlEncoded.get("interests").head)
    val newAccount = Account(request.body.asFormUrlEncoded.get("email").head,
      Account.hashPasswordPlusSalt(request.body.asFormUrlEncoded.get("password").head),
      newProfile, Account.hashPassword(
        request.body.asFormUrlEncoded.get("adminpassword").head, "") ==  AccountManager.adminHash)
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
}