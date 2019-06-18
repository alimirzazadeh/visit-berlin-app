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
    if (HomeController.logaccount.email == "example@example.com") {
      Ok(views.html.index(null))
    } else {
      Ok(views.html.index(HomeController.logaccount))
    }
  }


  def manageAccount = Action {
    if (HomeController.logaccount.email != "example@example.com") {
      Ok(views.html.manageAccount(HomeController.logaccount, assetsFinder))
    } else {
      Ok(views.html.index(null))
    }
  }

  def register = Action {
    if (HomeController.logaccount.email == "example@example.com") {
      Ok(views.html.register(assetsFinder))
    } else {
      Ok(views.html.index(HomeController.logaccount));
    }
  }

  def login = Action {
    Ok(views.html.login(null))
  }

  def logout = Action {
    HomeController.logaccount = Account("example@example.com", ("nothing", "none"), Profile("NOT", "NO", 1999, "NOWW", "None"), false);
    Ok(views.html.index(null))
  }

  def place = Action {
    Ok(views.html.placepage("Account", assetsFinder))
  }
  def afteredit = Action { implicit request =>
    val newProfile = Profile(request.body.asFormUrlEncoded.get("firstname").head.toUpperCase,
      request.body.asFormUrlEncoded.get("lastname").head.toUpperCase,
      request.body.asFormUrlEncoded.get("birthyear").head.toInt,
      request.body.asFormUrlEncoded.get("hometown").head.toUpperCase,
      request.body.asFormUrlEncoded.get("interests").head)
    val newAccount = Account(request.body.asFormUrlEncoded.get("email").head,
      HomeController.logaccount.saltedHash,
      newProfile,
      // Change this to check user input match to the admin password's salt-free hash
      admin=true)
    val am = new AccountManager
    am.editAccount(HomeController.logaccount, newAccount);
    HomeController.logaccount = newAccount;
    Ok(views.html.index(newAccount));
  }
  def edit = Action {
    Ok(views.html.manageAccount(HomeController.logaccount, assetsFinder))
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
      newProfile,
      // Change this to check user input match to the admin password's salt-free hash
      //<<<<<<< HEAD
      //      newProfile, Account.hashPassword(
      //        request.body.asFormUrlEncoded.get("adminpassword").head, "") ==  AccountManager.adminHash)
      //=======
      admin=true)
    System.out.println(newAccount);
    am.writeToCSV(am.addAccount(newAccount))
    HomeController.logaccount = newAccount;
    Ok(views.html.index(newAccount))
  }

  def afterlogin = Action { implicit request =>
    val email = request.body.asFormUrlEncoded.get("email").head
    val password = request.body.asFormUrlEncoded.get("password").head
    System.out.println(email);
    val am = new AccountManager
    val accountTest = am.verifyLogin(email, password)
    accountTest match {
      case None => Ok(views.html.login("INCORRECT PASSWORD"))
      case Some(userAccount) => {
        HomeController.logaccount = userAccount
        Ok(views.html.index(userAccount))
        //HomeController.logaccount.changeEmail(userAccount.email)

      }
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

object HomeController {
  var logaccount = new Account("example@example.com", ("nothing", "none"), Profile("NOT", "NO", 1999, "NOWW", "None"), false);
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