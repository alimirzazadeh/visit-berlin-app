package controllers

class Profile(val firstName: String, val lastName: String) {

  require(s"$firstName $lastName".matches("^([A-Z]+('?[A-Z]*?|( ?|-?)(?=[A-Z])))+$"),
    "Name may only contain capital letters and singular diacriticals.")

}
