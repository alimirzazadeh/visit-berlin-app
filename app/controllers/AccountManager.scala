package controllers

import java.io.File
import java.io.PrintWriter
import scala.io.Source

class AccountManager {

  def readFromCSV: List[Account] = {
    def createList(accList: List[Account], csvInfo: Iterator[String]): List[Account] = {
      if (csvInfo.hasNext) {
        val info = csvInfo.next().split(",")
        createList(Account(info(0), info(1), new Profile(info(2), info(3))) :: accList, csvInfo)
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
    writer.write("firstname,lastname,email,username,password\n")
    for (account <- accList) {
      val current = account.makeStringList
      for (item <- 0 to 3) writer.write(current(item) + ",")
      writer.write(current(4) + '\n')
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
    val a1 = new Account("npoulos9825@gmail.com", "coolPassword!",
      new Profile("NICK", "POULOS"))
    println(a1.passwordHash)
    println(a1.salt)
    val a2 = a1.changePassword("veryCoolPassword!!")
    println(a2.passwordHash)
    println(a2.salt)
    println(Account.generateSalt(8))
  }
}