package controllers

import java.security.MessageDigest
import scala.util.Random

/*
  * Class containing all data for a user's account as well as the functions to cryptographically
  * secure user passwords and edit existing data. Account instances contain all relevant user
  * data, including a personal profile, and form the structure for registering, logging in, and
  * using the web application's services as either a regular user or an administrator with editing
  * privileges. They do not contain raw user passwords anywhere, but rather only store hashed
  * passwords and associated cryptographic salts for sake of data security. The user is capable
  * of changing all fields within their account once they register one, provided the inputted
  * email is always unique and of proper form. The addition of a random salt string in hashing
  * protects user accounts that coincidentally register with the same raw password.
  * @param email user's email String inputted at registration, must match proper email format
  * @param saltedHash String tuple containing a randomly generated salt string and the resultant
  *                   hash from performing SHA-256 on the user's inputted password plus the salt
  * @param profile instance of a profile class containing all personal data inputted at registration
  * @param admin a Boolean denoting whether or not the account has admin status when it is created
  */
case class Account(email: String, saltedHash: (String, String), profile: Profile, admin: Boolean) {

  /*
    * Performs a regex matching operation to ensure the email String is in proper email format
    */
  require(
    email.matches("^([\\w\\d!#$%&'*+-\\/=?^`{|}~]+(\\.?(?=[\\w\\d]))[\\w\\d]*?)+" +
      "@([a-zA-Z0-9]+((-?)+(?=[a-zA-Z0-9])[a-zA-Z0-9])*?)+\\.([a-z]+)$") &&
    email.split("@")(0).length() <= 64 &&
    email.split("@")(1).length() <= 255, "Email address is of improper format.")

  /*
    * Submits a new user account containing the provided new email
    * @param newEmail new email String provided by the user during editing
    * @return the updated account
    */
  def changeEmail(newEmail: String): Account = copy(email = newEmail)

  /*
    * Submits a new user account containing the new password hash with a new salt
    * @param newPassword new raw password String provided by the user during editing,
    *                    given to the same hash function with a newly generated salt
    * @return the updated account
    */
  def changePassword(newPassword: String): Account = copy(saltedHash = Account.hashPasswordPlusSalt(newPassword))

  /*
    * Submits a list of Strings containing the account email, password hash, salt, and admin value
    * @return the account data transformed to a String list
    */
  def toList: List[String] = {
    List(s"$email", s"${saltedHash._1}", s"${saltedHash._2}", s"$admin")
  }

  /*
    * Overrides generic equals method to check the equality of two accounts by the equality
    * of their email Strings and password hash Strings, as identical hashes imply identical salts
    * @param other the Object being checked for equality to this one
    * @return true if the accounts have the same email and password hash, and false otherwise
    */
  override def equals(other: Any): Boolean = {
    other match {
      case that: Account =>
        this.email == that.email && this.saltedHash == that.saltedHash
      case _ => false
    }
  }

  /*
    * Overrides generic toString method to return the relevant data of the account as a String
    * @return a String containing the account email, password hash, and salt
    */
  override def toString: String = {
    s"Email: $email, Password Hash: ${saltedHash._1}, Salt: ${saltedHash._2}"
  }
}

/*
  * Companion object for Account class containing non-instance-specific functions for
  * generating salts and cryptographically hashing password Strings
  */
object Account {

  /*
    * Generate a random salt String and hash the raw password with the salt appended
    * @param password the raw password String provided by user input
    * @return a tuple containing the password hash and its associated salt
    */
  def hashPasswordPlusSalt(password: String): (String, String) = {
    val localSalt = generateSalt
    (hashPassword(password, localSalt), localSalt)
  }

  /*
    * Performs an SHA-256 cryptographic hash on the String representing the raw password
    * with a given salt appended to it
    * @param password the raw password String provided by user input
    * @param salt a String appended to the raw password before hashing to heighten security,
    *             will be in the format of a randomly generated eight-length alphanumeric chunk
    * @return a String containing the resultant hash of the password with the salt appended
    */
  def hashPassword(password: String, salt: String): String = {
    MessageDigest.getInstance("SHA-256")
      .digest(s"$password$salt".getBytes("UTF-8"))
      .map("%02x".format(_)).mkString
  }

  /*
    * Generates a random alphanumeric String of variable length by randomly selecting that
    * number of characters with replacement from the given sample string, then making the
    * character list into a String
    * @param n the variable length of the generated String
    * @return a random alphanumeric salt of length n
    */
  def generateSalt(n: Int): String = {
    val allowed = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"
    val rand = new Random()
    def generateSalt(n: Int, saltLetters: List[Char]): List[Char] = {
      n match {
        case 0 => saltLetters
        case _ => generateSalt(n - 1, allowed.charAt(rand.nextInt(36)) :: saltLetters)
      }
    }
    generateSalt(n, List()).mkString
  }

  /*
    * Generates an eight-length salt
    * @return the conventional result of the generateSalt function with length parameter 8
    */
  def generateSalt: String = generateSalt(8)
}