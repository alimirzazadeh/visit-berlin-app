package controllers

import java.io.File
import java.io.PrintWriter
import scala.io.Source

class AccountManager {

  def readFromCSV: List[Account] = {
    def createList(accList: List[Account], csvInfo: Iterator[String]): List[Account] = {
      if (csvInfo.hasNext) {
        val info = csvInfo.next().split(",")
        var isAdmin = false
        if (info(3) == "true") isAdmin = true
        createList(Account(info(0), info(1), info(2), Profile(info(4), info(5), info(6).toInt, info(7), info(8)), isAdmin) :: accList, csvInfo)
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
    val updatedList = if (!findAccount(accList, newAcc)) accList else newAcc :: accList.filter(_ != oldAcc)
    updatedList
  }

  def findAccount(accList: List[Account], account: Account): Boolean = {
    var found = false
    for (acc <- accList) {
      if (acc.email == account.email)
        found = true
    }
    found
  }
}

object AccountManager {

  val filename: String = "public/accounts.csv"

  def main(args: Array[String]): Unit = {
    val am1 = new AccountManager
    am1.writeToCSV(am1.addAccount(Account("tam@gmail.com","testPassHash", Account.generateSalt(), Profile("TYLER","SCARAMASTRO",1999,"Tennessee","Nothing lol"), admin=true)))
  }
//  def addAccount(acc: Account): Unit = {
//    val aml = new AccountManager
//    aml.writeToCSV(aml.addAccount(acc))
//  }
}