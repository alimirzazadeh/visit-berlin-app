package controllers

case class Profile(firstName: String, lastName: String, birthYear: Int, hometown: String, interests: String) {

  // Name fields provided will already be trimmed and made uppercase
  require(s"$firstName $lastName".matches("^([A-Z]+('?[A-Z]*?|( ?|-?)(?=[A-Z])))+$"),
    "Name may only contain capital letters and singular diacriticals.")

  //birthYear must be between 1900 and 2019, inclusive
  require(birthYear <= 2019 && birthYear > 1900)

  def changeFirstName(newFirstName: String): Profile = new Profile(newFirstName, lastName, birthYear, hometown, interests)
  def changeLastName(newLastName: String): Profile = new Profile(firstName, newLastName, birthYear, hometown, interests)
  def changeBirthYear(newBirthYear: Int): Profile = new Profile(firstName, lastName, newBirthYear, hometown, interests)
  def changeHometown(newHometown: String): Profile = new Profile(firstName, lastName, birthYear, newHometown, interests)
  def changeInterests(newInterests: String): Profile = new Profile(firstName, lastName, birthYear, hometown, newInterests)


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

}
