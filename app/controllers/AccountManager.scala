package controllers

import java.io.File
import java.io.PrintWriter
import scala.io.Source

class AccountManager {

  /* We wouldn't normally include this in the source code for security purposes, but this string
  represents the salt-free cryptographic hash of our admin password, "scalaisthefuture"*/
  final val adminHash: String = "ab867cba53df0946b0c0bf0084503f8c70ff995fa615ee2f131cacf45c60cb15"

  /** Uses tail recursion to read a CSV file with all of the information of the accounts
    * @return a list of accounts that can be easily used by other functions
   */
  def readFromCSV: List[Account] = {
    /**
      * Takes a line from a csv file string iterator and converts it into an account object, which is then
      * added to a list and returned
      * @param accList current list of account objects
      * @param csvInfo the iterator for the csv file
      * @return an updated list of accounts after one iteration from the iterator
      */
    def createList(accList: List[Account], csvInfo: Iterator[String]): List[Account] = {
      if (csvInfo.hasNext) {
        val info = csvInfo.next().split(",")
        /*Different indexes in the info list are specific pieces of information in the csv file,
         *such as whether or not the account is an admin. This allows the csv file to make sense when
         *looked at by a human, as everything is in order
         */
        val isAdmin = info(3) == "true"
        createList(Account(info(0), (info(1), info(2)),
          Profile(info(4), info(5), info(6).toInt, info(7), info(8)), isAdmin) :: accList, csvInfo)
      }
      else
        accList
    }
    val file = new File(AccountManager.filename)
    if (file.exists())
      createList(List(), Source.fromFile(AccountManager.filename).getLines.drop(1))
    else {
      writeToCSV(List())
      List()
    }
  }

  /**
    * Takes a list of accounts and writes them into a csv file
    * @param accList A list of account objects
    */
  def writeToCSV(accList: List[Account]): Unit = {
    val writer = new PrintWriter(new File(AccountManager.filename))
    writer.write("email,passwordHash,salt,admin,firstName,lastName,birthYear,hometown,interests\n")
    for (account <- accList) {
      val currentAcc = account.toList
      val currentProf = account.profile.toList
      for (item <- 0 to 3) writer.write(currentAcc(item) + ",")
      for (item <- 0 to 3) writer.write(currentProf(item) + ",")
      writer.write(currentProf(4) + '\n')
    }
    writer.close()
  }

  /**
    * Gives a new list of account objects, which is updated with a new account if that account does not exist
    * @param acc a new account object to be added to the list
    * @return the original list of accounts if the account already exists, otherwise a new list including the new account
    */
  def addAccount(acc: Account): List[Account] = {
    val accList = readFromCSV
    val updatedList = if (findAccount(accList, acc)) accList else acc :: accList
    updatedList
  }

  /**
    * Filters an account object out of a list of accounts if the account exists
    * @param acc account to be deleted
    * @return a list of accounts without the account passed as a parameter. If the account doesn't exist, return the original list
    */
  def removeAccount(acc: Account): List[Account] = {
    val accList = readFromCSV
    val updatedList = if (!findAccount(accList, acc)) accList else accList.filter(_.email != acc.email)
    updatedList
  }

  /**
    * Filters an old account object out of a list of accounts and then adds a new account instead, allowing users to
    * change data in their account and still maintain their account in the csv file
    * @param oldAcc the original account
    * @param newAcc the account to be added to the account list
    * @return an updated list of accounts after removing oldAcc and adding newAcc
    */
  def editAccount(oldAcc: Account, newAcc: Account): List[Account] = {
    val accList = readFromCSV
    val updatedList = if (!findAccount(accList, oldAcc)) accList else newAcc :: accList.filter(_.email != oldAcc.email)
    updatedList
  }

  /**
    * Decides whether an account exists in an account list based off of the account email
    * @param accList the list of accounts to be searched
    * @param account the account that may be found by the method
    * @return true if the account exists in the accList, otherwise return false
    */
  def findAccount(accList: List[Account], account: Account): Boolean = {
    accList.exists(_.email == account.email)
  }

  /**
    * Using an email and a password, find if the account exists, find if the password is correct, and return the
    * matching account object from the csv file if the email and password matches the account
    * @param email the email of the account the user is attempting to log into
    * @param password the password given by the user that will be checked against the account
    * @return an option that either contains the account if credentials match or None if credentials don't match
    */
  def verifyLogin(email: String, password: String): Option[Account] = {
    val accList = readFromCSV
    val matchList = accList.filter(_.email == email)
    if (matchList.isEmpty) None else {
      val checkAccount = matchList.head
      if (Account.hashPassword(password, checkAccount.saltedHash._2) != checkAccount.saltedHash._1) None else
        Some(checkAccount)
    }
  }
}

/**
  * AccountManager singleton object which has a static filename variable that can be used in methods within the class
  * Also contains a main method which is used to test different methods on a smaller scale and can be easily run
  * within IntelliJ
  */
object AccountManager {

  val filename: String = "public/accounts.csv"

  def main(args: Array[String]): Unit = {
    val am = new AccountManager
    val a1 = Account("tam@gmail.com", Account.hashPasswordPlusSalt("testPassword"),
      Profile("TYLER","SCARAMASTRO",1999,"Tennessee","Nothing lol"), admin=true)
    val a2 = a1.changeEmail("tyfjdsakl@gjfklds.com")
    // am.writeToCSV(am.addAccount(a1))
    am.writeToCSV(am.editAccount(a1, a2))
  }
}