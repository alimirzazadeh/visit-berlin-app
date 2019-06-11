import scala.util.hashing.MurmurHash3

class Account(val firstName: String, val lastName: String, val email: String, val username: String,
              val passwordHash: Long) {

  // Name fields provided will already be trimmed and made uppercase
  require(s"$firstName $lastName".matches("^([A-Z]+('?[A-Z]*?|( ?|-?)(?=[A-Z])))+$"),
    "Name may only contain letters and singular diacriticals.")

  require(
    email.matches("^([\\w\\d!#$%&'*+-\\/=?^`{|}~]+(\\.?(?=[\\w\\d]))[\\w\\d]*?)+" +
      "@([a-zA-Z0-9]+((-?)+(?=[a-zA-Z0-9])[a-zA-Z0-9])*?)+\\.([a-z]+)$") &&
    email.split("@")(0).length() <= 64 &&
    email.split("@")(1).length() <= 255, "Email address is of improper format.")

  def changeFirstName(newFirstName: String): Account = {
    Account(newFirstName, lastName, email, username, passwordHash)
  }

  def changeLastName(newLastName: String): Account = {
    Account(firstName, newLastName, email, username, passwordHash)
  }

  def changeUsername(newUsername: String): Account = {
    Account(firstName, lastName, email, newUsername, passwordHash)
  }

  def changePassword(newPassword: String): Account = {
    Account(firstName, lastName, email, username, newPassword)
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

  def apply(firstName: String, lastName: String, email: String, username: String, passwordHash: Long): Account = {
    new Account(firstName, lastName, email, username, passwordHash)
  }

  def apply(firstName: String, lastName: String, email: String, username: String, password: String): Account = {
    new Account(firstName, lastName, email, username, hashPassword(password))
  }

  def main(args: Array[String]): Unit = {
    val a1 = Account("NICK", "POULOS", "npoulos9825@gmail.com", "npoulos3", "3L3ctr0m4gn3t1$m")
    println(a1)
  }
}