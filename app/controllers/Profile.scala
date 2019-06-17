package controllers

/**
  * Class containing all data for a user's profile and the functions to edit existing data
  * @param firstName user's first name String inputted at registration and made uppercase
  * @param lastName user's last name String inputted at registration and made uppercase
  * @param birthYear user's birth year Int inputted at registration
  * @param hometown user's hometown String inputted at registration
  * @param interests user's interests represented in a String inputted at registration
  */
case class Profile(firstName: String, lastName: String, birthYear: Int, hometown: String, interests: String) {

  /**
    * Performs a regex operation to ensure the name Strings are uppercase and match
    * conventional format for real human names
    */
  require(s"$firstName $lastName".matches("^([A-Z]+('?[A-Z]*?|( ?|-?)(?=[A-Z])))+$"),
    "Name may only contain capital letters and singular diacriticals.")

  /**
    * Birth year must be between 1900 and 2019, inclusive
    */
  require(birthYear <= 2019 && birthYear > 1900)

  def changeFirstName(newFirstName: String): Profile = copy(firstName = newFirstName)
  def changeLastName(newLastName: String): Profile = copy(lastName = newLastName)
  def changeBirthYear(newBirthYear: Int): Profile = copy(birthYear = newBirthYear)
  def changeHometown(newHometown: String): Profile = copy(hometown = newHometown)
  def changeInterests(newInterests: String): Profile = copy(interests = newInterests)

  override def equals(that: Any): Boolean = {
    that match {
      case that: Profile => {
        this.firstName == that.firstName &&
          this.lastName == that.lastName  &&
          this.birthYear == that.birthYear &&
          this.hometown == that.hometown &&
          this.interests == that.interests
      }
      case _ => false
    }
  }

  def toList: List[String] = {
    this.firstName :: this.lastName :: this.birthYear.toString() :: this.hometown :: this.interests :: List[String]()
  }
}
