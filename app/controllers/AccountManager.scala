package controllers

import java.io.File
import java.io.PrintWriter
import scala.io.Source

class AccountManager {

  /* We wouldn't normally include this in the source code for security purposes, but this string
  represents the salt-free cryptographic hash of our admin password, "scalaisthefuture"*/
  final val adminHash: String = "ab867cba53df0946b0c0bf0084503f8c70ff995fa615ee2f131cacf45c60cb15"

  def readFromCSV: List[Account] = {
    def createList(accList: List[Account], csvInfo: Iterator[String]): List[Account] = {
      if (csvInfo.hasNext) {
        val info = csvInfo.next().split(",")
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

  def addAccount(acc: Account): List[Account] = {
    val accList = readFromCSV
    val updatedList = if (findAccount(accList, acc)) accList else acc :: accList
    updatedList
  }

  def removeAccount(acc: Account): List[Account] = {
    val accList = readFromCSV
    val updatedList = if (!findAccount(accList, acc)) accList else accList.filter(_ != acc)
    updatedList
  }

  def editAccount(oldAcc: Account, newAcc: Account): List[Account] = {
    val accList = readFromCSV
    val updatedList = if (!findAccount(accList, oldAcc)) accList else newAcc :: accList.filter(_ != oldAcc)
    updatedList
  }

  def findAccount(accList: List[Account], account: Account): Boolean = {
    accList.exists(_.email == account.email)
  }

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

object AccountManager {

  val filename: String = "public/accounts.csv"

  def main(args: Array[String]): Unit = {
    val am = new AccountManager
    am.writeToCSV(am.addAccount(Account("tam@gmail.com", Account.hashPasswordPlusSalt("testPassword"),
      Profile("TYLER","SCARAMASTRO",1999,"Tennessee","Nothing lol"), admin=true)))
    am.writeToCSV(am.addAccount(Account("npoulos69@hotmail.gov", Account.hashPasswordPlusSalt("testPassword"),
      Profile("TYLER","SCARAMASTRO",1999,"Tennessee","Nothing lol"), admin=true)))
    am.writeToCSV(am.addAccount(Account("tam@gmail.com", Account.hashPasswordPlusSalt("testPassword"),
      Profile("TYLER","SCARAMASTRO",1999,"Tennessee","Nothing lol"), admin=true)))
    println(am.verifyLogin("tam@gmail.com", "testPassword").isDefined)
  }
//  def addAccount(acc: Account): Unit = {
//    val aml = new AccountManager
//    aml.writeToCSV(aml.addAccount(acc))
//  }
}