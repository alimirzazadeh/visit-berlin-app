package controllers

import java.io.File
import java.io.PrintWriter
import scala.io.Source

class AccountManager {

  def readFromCSV: List[Account] = {
    def createList(accList: List[Account], csvInfo: Iterator[String]): List[Account] = {
      if (csvInfo.hasNext) {
        val info = csvInfo.next().split(",")
        createList(Account(info(0), info(1), info(2), info(3), info(4)) :: accList, csvInfo)
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
    val updatedList = if (accList.contains(acc)) accList else acc :: accList //this doesn't work - duplicates are still added
    //doesn't work because the string version of password hash is different each time -- look at target/accounts.csv
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
  
  // Run this main method to manually test Account and AccountManager instances and functions
  // If readFromCSV is not implemented at time of test, a dummy return must be provided for it
  def main(args: Array[String]) = {
    val am1 = new AccountManager
    am1.writeToCSV(am1.addAccount(Account("TYLER","SCAR","tscaram@gmail.com","tscaram","test2")))
    am1.writeToCSV(am1.addAccount(Account("TYLER","SCAR","tscaram@gmail.com","tscaram","test2")))
    am1.writeToCSV(am1.addAccount(Account("TYLER","SCAR","tscaram@gmail.com","tscaram","test2")))

    //am1.writeToCSV(am1.removeAccount(Account("TYLER","SCAR","tscaram@gmail.com","tscaram","test2")))
    //looks like add, remove, and edit don't work because the string version of the hash is random in the csv
  }
}