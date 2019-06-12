package controllers

import java.security.MessageDigest
import scala.util.Random

case class Account(val email: String, password: String, val profile: Profile) {

  val salt = Account.generateSalt
  val passwordHash = Account.hashPassword(password, salt)

  require(
    email.matches("^([\\w\\d!#$%&'*+-\\/=?^`{|}~]+(\\.?(?=[\\w\\d]))[\\w\\d]*?)+" +
      "@([a-zA-Z0-9]+((-?)+(?=[a-zA-Z0-9])[a-zA-Z0-9])*?)+\\.([a-z]+)$") &&
    email.split("@")(0).length() <= 64 &&
    email.split("@")(1).length() <= 255, "Email address is of improper format.")

  def changeEmail(newEmail: String) = {
    this.copy(email = newEmail)
  }

  def changePassword(newPassword: String) = {
    this.copy(password = newPassword)
  }

  def makeStringList: List[String] = {
    List(s"$email", s"$passwordHash", s"$salt")
  }

  override def equals(other: Any): Boolean = {
    other match {
      case that: Account =>
        this.email == that.email && this.passwordHash == that.passwordHash
      case _ => false
    }
  }

  override def toString: String = {
    s"Email: $email, Password Hash: $passwordHash"
  }
}

object Account {

  def hashPassword(password: String, salt: String): String = {
    MessageDigest.getInstance("SHA-256")
      .digest(s"$password$salt".getBytes("UTF-8"))
      .map("%02x".format(_)).mkString
  }

  def generateSalt: String = {
    val allowed = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"
    val rand = new Random()
    val saltLetters: List[Char] = List()
    for (i <- 0 to 7) {
      allowed.charAt(rand.nextInt(36)) :: saltLetters
    }
    saltLetters.mkString
  }
}