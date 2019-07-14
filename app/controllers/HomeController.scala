package controllers

import javax.inject._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models.UserData

/**
  * Class containing the controlling logic for the different pages on
  * the Berlin website and their navigation
  */
class HomeController @Inject()(cc: ControllerComponents)(implicit assetsFinder: AssetsFinder)
  extends AbstractController(cc) {

  def hello = Action {
    Ok(views.html.hello())
  }

  def createReview = Action {
    Ok(views.html.createreview("", false))
  }

  def before = Action {
    Ok(views.html.hello())
  }

  def beforelogin = Action {
    Ok(views.html.login(null, falseRegistration = false))
  }

  // Home Page
  def index = Action {
    val attMan = new AttractionManager()
    attMan.createDefaultAttractions()
    if (HomeController.logaccount.email == "example@example.com") {
      Ok(views.html.index(null, attMan.readFromCSV))
    } else {
      Ok(views.html.index(HomeController.logaccount, attMan.readFromCSV))
    }
  }

  def changepage(id: String) = Action {
    val attMan = new AttractionManager();
    var attraction = attMan.attractionFromName(id);
    if (attraction == null) {
      attraction = Attraction("a","a","a","a")
    }
    Ok(views.html.changepages(null, assetsFinder, attraction))
  }

  def manageAccount = Action {
    val attMan = new AttractionManager()
    if (HomeController.logaccount.email != "example@example.com") {
      Ok(views.html.manageAccount(HomeController.logaccount, assetsFinder))
    } else {
      Ok(views.html.index(null, attMan.readFromCSV))
    }
  }

  def register = Action {
    val attMan = new AttractionManager
    if (HomeController.logaccount.email == "example@example.com") Ok(views.html.register(
      assetsFinder, wrongAdminPassword = false)) else
      Ok(views.html.index(HomeController.logaccount, attMan.readFromCSV))
  }

  def login = Action {
    Ok(views.html.login(null, falseRegistration = false))
  }

  def changepassword = Action {
    Ok(views.html.changepassword(null))
  }

  def logout = Action {
    val attractions = new AttractionManager().readFromCSV
    HomeController.logaccount = Account("example@example.com", ("nothing", "none"), Profile("NOT", "NO", 1999, "NOWW", "None"), false)
    Ok(views.html.index(null, attractions))
  }

  def place(id: String) = Action {
    val attMan = new AttractionManager()
    val revMan = new ReviewManager()
    val attraction = attMan.attractionFromName(id)
    Ok(views.html.placepage("Account", assetsFinder, attraction, HomeController.logaccount, revMan.supplyReviews(attraction.attractionID)))
      //change this to id instead of name eventually
  }
  /**
    * Collects the information from the registration form to create an account
    */
  def afteredit = Action { implicit request =>
    val attractions = new AttractionManager().readFromCSV
    val newProfile = Profile(request.body.asFormUrlEncoded.get("firstname").head.toUpperCase,
      request.body.asFormUrlEncoded.get("lastname").head.toUpperCase,
      request.body.asFormUrlEncoded.get("birthyear").head.toInt,
      request.body.asFormUrlEncoded.get("hometown").head.toUpperCase,
      request.body.asFormUrlEncoded.get("interests").head)
    val newAccount = Account(request.body.asFormUrlEncoded.get("email").head,
      HomeController.logaccount.saltedHash,
      newProfile,
      // Change this to check user input match to the admin password's salt-free hash
      admin = true)
    val accMan = new AccountManager
    accMan.writeToCSV(accMan.editAccount(HomeController.logaccount, newAccount));
    HomeController.logaccount = newAccount;
    Ok(views.html.index(newAccount, attractions));
  }

  def aftereditpass = Action { implicit request =>
    val accMan = new AccountManager
    val attractions = new AttractionManager().readFromCSV
    if (request.body.asFormUrlEncoded.get("password").head == request.body.asFormUrlEncoded.get("password2").head) {
      accMan.writeToCSV(accMan.editAccount(HomeController.logaccount, HomeController.logaccount.changePassword(
        request.body.asFormUrlEncoded.get("password").head
      )));
      HomeController.logaccount.changePassword(request.body.asFormUrlEncoded.get("password").head)
      Ok(views.html.index(HomeController.logaccount, attractions))
    } else {
      Ok(views.html.changepassword("PASSWORDS MUST MATCH"))
    }
  }

  def afterReview = Action { implicit request =>
    val rm = new ReviewManager()
    val attman = new AttractionManager()
    val attraction = attman.attractionFromID(request.body.asFormUrlEncoded.get("attractionID").head.toInt)
    val review = new Review(request.body.asFormUrlEncoded.get("title").head, request.body.asFormUrlEncoded.get("body").head,
      request.body.asFormUrlEncoded.get("authorEmail").head, request.body.asFormUrlEncoded.get("rating").head.toInt,
      request.body.asFormUrlEncoded.get("attractionID").head.toInt)
    rm.writeToCSV(rm.addReview(review))
    System.out.println(attman.attractionFromID(request.body.asFormUrlEncoded.get("attractionID").head.toInt))
    Ok(views.html.placepage("Account", assetsFinder, attraction, HomeController.logaccount, rm.supplyReviews(attraction.attractionID)))
  }


  def afterEditAttraction = Action { implicit request =>
    val attMan = new AttractionManager
    val rm = new ReviewManager
    val oldPage = attMan.attractionFromName(request.body.asFormUrlEncoded.get("oldName").head)
    val newPage = Attraction(request.body.asFormUrlEncoded.get("name").head, //change thiss!!!!!!
      request.body.asFormUrlEncoded.get("pictureURL").head, request.body.asFormUrlEncoded.get("description").head,
      request.body.asFormUrlEncoded.get("location").head)
    if (oldPage.name == "a")
      attMan.writeToCSV(attMan.addAttraction(newPage))
    else
      attMan.writeToCSV(attMan.editAttraction(oldPage, newPage))
      Ok(views.html.placepage("idk", assetsFinder, newPage, HomeController.logaccount, rm.supplyReviews(newPage.attractionID)))
  }

  def edit = Action {
    Ok(views.html.manageAccount(HomeController.logaccount, assetsFinder))
  }

  def after = Action { implicit request =>
    val accMan = new AccountManager
    val attractions = new AttractionManager().readFromCSV
    val newProfile = Profile(request.body.asFormUrlEncoded.get("firstname").head.toUpperCase,
      request.body.asFormUrlEncoded.get("lastname").head.toUpperCase,
      request.body.asFormUrlEncoded.get("birthyear").head.toInt,
      request.body.asFormUrlEncoded.get("hometown").head.toUpperCase,
      request.body.asFormUrlEncoded.get("interests").head)
    val adminInput = if (request.body.asFormUrlEncoded.get("adminpassword").head != "password") {
      Account.hashPassword(request.body.asFormUrlEncoded.get("adminpassword").head, "")
    } else "password"
    val newAccount = Account(request.body.asFormUrlEncoded.get("email").head,
      Account.hashPasswordPlusSalt(request.body.asFormUrlEncoded.get("password").head),
      newProfile, adminInput == AccountManager.adminHash)
    if (adminInput != "password" && adminInput != AccountManager.adminHash) {
      Ok(views.html.register(assetsFinder, true))
    } else if (accMan.findAccount(accMan.readFromCSV, newAccount)) {
      Ok(views.html.login(null, true))
    } else {
      accMan.writeToCSV(accMan.addAccount(newAccount))
      HomeController.logaccount = newAccount
      Ok(views.html.index(newAccount, attractions))
    }
  }

  /**
    * Logic for verifying a correct login with the correct password and email
    */
  def afterlogin = Action { implicit request =>
    val attractions = new AttractionManager().readFromCSV
    val email = request.body.asFormUrlEncoded.get("email").head
    val password = request.body.asFormUrlEncoded.get("password").head
    val accMan = new AccountManager
    val accountTest = accMan.verifyLogin(email, password)
    accountTest match {
      case None => Ok(views.html.login("INCORRECT PASSWORD", falseRegistration = false))
      case Some(userAccount) =>
        HomeController.logaccount = userAccount
        Ok(views.html.index(userAccount, attractions))
        //HomeController.logaccount.changeEmail(userAccount.email)

      }
  }

  /**
    * The structure of the user form for registration
    */
  val userForm = Form(
    mapping(
      "firstname" -> text,
      "lastname" -> text,
      "email" -> text,
      "hometown" -> text,
      "interests" -> text,
      "password" -> text,
      "birthyear" -> number
    )(UserData.apply)(UserData.unapply)
  )
}

object HomeController {
  var logaccount = new Account("example@example.com", ("nothing", "none"), Profile("NOT", "NO", 1999, "NOWW", "None"), false)
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
