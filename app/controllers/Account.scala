package controllers

import java.security.MessageDigest

import scala.util.Random

class Account(val email: String, val passwordHash: String, val salt: String) {

  def this(email: String, username: String, password: String) {
    val localSalt = generateSalt
    this(email, Account.hashPassword(password, "hello"), localSalt)
  }

  require(
    email.matches("^([\\w\\d!#$%&'*+-\\/=?^`{|}~]+(\\.?(?=[\\w\\d]))[\\w\\d]*?)+" +
      "@([a-zA-Z0-9]+((-?)+(?=[a-zA-Z0-9])[a-zA-Z0-9])*?)+\\.([a-z]+)$") &&
    email.split("@")(0).length() <= 64 &&
    email.split("@")(1).length() <= 255, "Email address is of improper format.")

  def generateSalt: String = {
    val alloweds = List[Char]()
    val rand = new Random()
    val chars = List[Char]
    for (i <- 0 to 7) {

    }
  }

  def makeStringList: List[String] = {
    List(s"$email", s"$passwordHash", s"$salt")
  }

  override def equals(other: Any): Boolean = {
    other match {
      case that: Account =>
        this.firstName == that.firstName &&
          this.lastName == that.lastName &&
          this.username == that.username &&
          this.passwordHash == that.passwordHash
      case _ => false
    }
  }

  override def toString: String = {
    s"User $firstName $lastName -- Email: $email, Username: $username, Password Hash: $passwordHash"
  }
}
<<<<<<< Updated upstream
/*
class AccountParameter(info: String)
case class F(info: String) extends AccountParameter(info)
case class L(info: String) extends AccountParameter(info)
case class U(info: String) extends AccountParameter(info)
case class P(info: String) extends AccountParameter(info)
*/
object Account {
  
  def hashPassword(password: String): Long = {
    MurmurHash3.stringHash(password)
  }
=======

object Account {

  def hashPassword(password: String, salt: String): String = {
    MessageDigest.getInstance("SHA-256")
      .digest(s"$password$salt".getBytes("UTF-8"))
      .map("%02x".format(_)).mkString
  }

//  def apply(firstName: String, lastName: String, email: String, username: String, passwordHash: String): Account = {
//    new Account(firstName, lastName, email, username, passwordHash)
//  }
//
//  def apply(firstName: String, lastName: String, email: String, username: String, password: String): Account = {
//    new Account(firstName, lastName, email, username, hashPassword(password, salt))
//  }
>>>>>>> Stashed changes
}