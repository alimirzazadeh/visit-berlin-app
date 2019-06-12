package controllers

import java.io.File
import java.io.PrintWriter
import scala.io.Source

class AccountManager {

  def readFromCSV: List[Account] = {
    def createList(accList: List[Account], csvInfo: Iterator[String]): List[Account] = {
      if (csvInfo.hasNext) {
        val info = csvInfo.next().split(",")
        createList(new Account(info(0), info(1), info(2), true, Profile(info(3), info(4), info(5).toInt, info(6), info(7))) :: accList, csvInfo)
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
    writer.write("email,passwordHash,salt,firstName,lastName,birthYear,hometown,interests\n")
    for (account <- accList) {
      val currentAcc = account.makeStringList
      val currentProf = account.profile.toList()
      for (item <- 0 to 2) writer.write(currentAcc(item) + ",")
      for (item <- 0 to 3) writer.write(currentProf(item) + ",")
      writer.write(currentProf(4) + '\n')
    }
    writer.close()
  }

  def addAccount(acc: Account): List[Account] = {
    val accList = readFromCSV
    val updatedList = if (accList.contains(acc)) accList else acc :: accList
    updatedList
  }

  def removeAccount(acc: Account): List[Account] = {
    val accList = readFromCSV
    val updatedList = if (!accList.contains(acc)) accList else accList.filter(_ != acc)
    updatedList
  }

  def editAccount(oldAcc: Account, newAcc: Account): List[Account] = {
    val accList = readFromCSV
    val updatedList = if (!accList.contains(oldAcc)) accList else newAcc :: accList.filter(_ != oldAcc)
    updatedList
  }
}

object AccountManager {

  val filename: String = "accounts.csv"

  def main(args: Array[String]) = {
    val am1 = new AccountManager
    am1.writeToCSV(am1.addAccount(new Account("tscaram@gmail.com","testPass", Account.generateSalt(), true, Profile("TYLER","SCARAMASTRO",1999,"Tennessee","Nothing lol"))))
  }
}