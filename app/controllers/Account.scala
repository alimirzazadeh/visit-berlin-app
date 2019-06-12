package controllers

import java.security.MessageDigest
import scala.util.Random

case class Account(email: String, passwordHash: String, salt: String, profile: Profile, admin: Boolean) {

  require(
    email.matches("^([\\w\\d!#$%&'*+-\\/=?^`{|}~]+(\\.?(?=[\\w\\d]))[\\w\\d]*?)+" +
      "@([a-zA-Z0-9]+((-?)+(?=[a-zA-Z0-9])[a-zA-Z0-9])*?)+\\.([a-z]+)$") &&
    email.split("@")(0).length() <= 64 &&
    email.split("@")(1).length() <= 255, "Email address is of improper format.")

  def changeEmail(newEmail: String): Account = {
    copy(email = newEmail)
  }

  def changePassword(newPassword: String): Account = {
    copy(passwordHash = Account.hashPassword(newPassword, salt))
  }

  def makeStringList: List[String] = {
    List(s"$email", s"$passwordHash", s"$salt", s"$admin")
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

  def generateSalt(): String = {
    generateSalt(8)
  }
}