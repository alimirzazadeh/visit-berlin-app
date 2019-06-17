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

  /**
    * Submits a new user profile containing the provided new first name
    *
    * @param newFirstName new first name String provided by the user during editing
    * @return the updated profile
    */
  def changeFirstName(newFirstName: String): Profile = copy(firstName = newFirstName)

  /**
    * Submits a new user profile containing the provided new last name
    *
    * @param newLastName new last name String provided by the user during editing
    * @return the updated profile
    */
  def changeLastName(newLastName: String): Profile = copy(lastName = newLastName)

  /**
    * Submits a new user profile containing the provided new birth year
    *
    * @param newBirthYear new birth year Int provided by the user during editing
    * @return the updated profile
    */
  def changeBirthYear(newBirthYear: Int): Profile = copy(birthYear = newBirthYear)

  /**
    * Submits a new user profile containing the provided new hometown
    *
    * @param newHometown new hometown String provided by the user during editing
    * @return the updated profile
    */
  def changeHometown(newHometown: String): Profile = copy(hometown = newHometown)

  /**
    * Submits a new user profile containing the provided new interests information
    *
    * @param newInterests new interests String provided by the user during editing
    * @return the updated profile
    */
  def changeInterests(newInterests: String): Profile = copy(interests = newInterests)

  /**
    * Overrides generic equals method to check the equality of two profiles by the equality
    * of all their fields - first name, last name, birth year, hometown, and interests
    *
    * @param other the Object being checked for equality to this one
    * @return true if the profiles have all the same fields, and false otherwise
    */
  override def equals(other: Any): Boolean = {
    other match {
      case that: Profile => {
        this.firstName == that.firstName &&
          this.lastName == that.lastName &&
          this.birthYear == that.birthYear &&
          this.hometown == that.hometown &&
          this.interests == that.interests
      }
      case _ => false
    }
  }

  /**
    * Submits a list of Strings containing the profile first and last name,
    * birth year, hometown, and interests
    * @return the profile data transformed to a String list
    */
  def toList: List[String] = {
    List(s"$firstName", s"$lastName", s"${birthYear.toString}", s"$hometown", s"$interests")
  }
}
