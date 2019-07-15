package controllers

import java.io.File
import java.io.PrintWriter
import scala.io.Source

/*
  * Class representing an AccountManager that is capable of writing user-generated accounts into
  * a persistent CSV file, reading the list of all valid accounts from said file, and performing
  * adds, removes, and edits as users register new accounts, change account details, or delete
  * their accounts. An AccountManager is instantiated in scenarios where the user is attempting
  * to create a new account or log into an existing one and contains the logic necessary to check
  * if emails exist within the persistent files, verify that the user inputs the correct password
  * by comparing cryptographic hashes, and update the persistent file according to each situation.
  */
class AccountManager {

  /* Uses tail recursion to read a CSV file with all of the information of the accounts
    * @return a list of accounts that can be easily used by other functions
   */
  def readFromCSV: List[Account] = {
    /*
      * Takes a line from a csv file string iterator and converts it into an account object, which
      * is then added to a list and returned
      * @param accounts current list of account objects
      * @param csvInfo the iterator for the csv file
      * @return an updated list of accounts after one iteration from the iterator
      */
    def createList(accounts: List[Account], csvInfo: Iterator[String]): List[Account] = {
      if (csvInfo.hasNext) {
        val info = csvInfo.next().split(",")
        /*Different indexes in the info list are specific pieces of information in the csv file,
         *such as whether or not the account is an admin. This allows the csv file to make sense when
         *looked at by a human, as everything is in order
         */
        val isAdmin = info(3) == "true"
        createList(Account(info(0), (info(1), info(2)),
          Profile(info(4), info(5), info(6).toInt, info(7), info(8)), isAdmin) :: accounts, csvInfo)
      }
      else
        accounts
    }
    val file = new File(AccountManager.filename)
    if (file.exists())
      createList(List(), Source.fromFile(AccountManager.filename).getLines.drop(1))
    else {
      writeToCSV(List())
      List()
    }
  }

  /*
    * Takes a list of accounts and writes them into a csv file
    * @param accounts A list of account objects
    */
  def writeToCSV(accounts: List[Account]): Unit = {
    val writer = new PrintWriter(new File(AccountManager.filename))
    writer.write("email,passwordHash,salt,admin,firstName,lastName,birthYear,hometown,interests\n")
    for (account <- accounts) {
      val currentAcc = account.toList
      val currentProf = account.profile.toList
      for (item <- 0 to 3) writer.write(currentAcc(item) + ",")
      for (item <- 0 to 3) writer.write(currentProf(item) + ",")
      writer.write(currentProf(4) + '\n')
    }
    writer.close()
  }

  /*
    * Gives a new list of account objects, which is updated with a new account if that account
    * does not yet exist within the file
    * @param acc a new account object to be added to the list
    * @return the original list of accounts if the account already exists, otherwise a new list
    *         including the new account
    */
  def addAccount(acc: Account): List[Account] = {
    val accounts = readFromCSV
    val updatedAccounts = if (findAccount(accounts, acc)) accounts else acc :: accounts
    updatedAccounts
  }

  /*
    * Filters an account object out of a list of accounts if the account exists
    * @param acc account to be deleted
    * @return a list of accounts without the account passed as a parameter. If the account
    *         doesn't exist, return the original list
    */
  def removeAccount(acc: Account): List[Account] = {
    val accounts = readFromCSV
    val updatedAccounts = if (!findAccount(accounts, acc)) accounts else accounts.filter(_.email != acc.email)
    updatedAccounts
  }

  /*
    * Filters an old account object out of a list of accounts and then adds a new account instead,
    * allowing users to change data in their account and still maintain their account in the csv file
    * @param oldAcc the original account
    * @param newAcc the account to be added to the account list
    * @return an updated list of accounts after removing oldAcc and adding newAcc
    */
  def editAccount(oldAcc: Account, newAcc: Account): List[Account] = {
    val accounts = readFromCSV
    val updatedAccounts = if (!findAccount(accounts, oldAcc)) accounts else newAcc :: accounts.filter(_.email != oldAcc.email)
    updatedAccounts
  }

  /*
    * Decides whether an account exists in an account list based off of the account email
    * @param accounts the list of accounts to be searched
    * @param account the account that may be found by the method
    * @return true if the account exists in the accounts, otherwise return false
    */
  def findAccount(accounts: List[Account], account: Account): Boolean = {
    accounts.exists(_.email == account.email)
  }

  /*
    * Using an email and a password, find if the account exists, find if the password is correct,
    * and return the matching account object from the csv file if the email and password matches the account
    * @param email the email of the account the user is attempting to log into
    * @param password the password given by the user that will be checked against the account
    * @return an option that either contains the account if credentials match or None otherwise
    */
  def verifyLogin(email: String, password: String): Option[Account] = {
    val accounts = readFromCSV
    val matchList = accounts.filter(_.email == email)
    if (matchList.isEmpty) None else {
      val checkAccount = matchList.head
      if (Account.hashPassword(password, checkAccount.saltedHash._2) != checkAccount.saltedHash._1) None else
        Some(checkAccount)
    }
  }
}

/*
  * Companion object for AccountManager which has a filename variable representing a csv file
  * containing all current user accounts that can be used in methods within the class.
  * Also contains a main method which is used to test different methods on a smaller scale and
  * can be easily run within IntelliJ
  */
object AccountManager {

  /*
    *  We wouldn't normally include this in the source code for security purposes, but this string
    *  represents the salt-free cryptographic hash of our admin password, "scalaisthefuture"
    */
  final val adminHash: String = "ab867cba53df0946b0c0bf0084503f8c70ff995fa615ee2f131cacf45c60cb15"

  /*
    * String location representing where our persistent file containing all user accounts resides
    */
  val filename: String = "public/accounts.csv"

  /*
    * Main method for running tests on accounts and account manager
    * @param args argument Strings potentially provided to name
    */
  def main(args: Array[String]): Unit = {
    val accMan = new AccountManager
  }
}