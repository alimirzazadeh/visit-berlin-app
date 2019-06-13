package controllers

import java.security.MessageDigest
import scala.util.Random

case class Account(email: String, saltedHash: (String, String), profile: Profile, admin: Boolean) {

  require(
    email.matches("^([\\w\\d!#$%&'*+-\\/=?^`{|}~]+(\\.?(?=[\\w\\d]))[\\w\\d]*?)+" +
      "@([a-zA-Z0-9]+((-?)+(?=[a-zA-Z0-9])[a-zA-Z0-9])*?)+\\.([a-z]+)$") &&
    email.split("@")(0).length() <= 64 &&
    email.split("@")(1).length() <= 255, "Email address is of improper format.")

  def changeEmail(newEmail: String): Account = copy(email = newEmail)
  def changePassword(newPassword: String): Account = copy(saltedHash = Account.hashPasswordPlusSalt(newPassword))

  def toList: List[String] = {
    List(s"$email", s"${saltedHash._1}", s"${saltedHash._2}", s"$admin")
  }

  override def equals(other: Any): Boolean = {
    other match {
      case that: Account =>
        this.email == that.email && this.saltedHash == that.saltedHash
      case _ => false
    }
  }

  override def toString: String = {
    s"Email: $email, Password Hash: ${saltedHash._1}, Salt: ${saltedHash._2}"
  }
}

object Account {

  def hashPasswordPlusSalt(password: String): (String, String) = {
    val localSalt = generateSalt()
    (hashPassword(password, localSalt), localSalt)
  }

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

  def generateSalt(): String = generateSalt(8)
}