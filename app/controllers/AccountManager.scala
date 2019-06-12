package controllers

import java.io.File
import java.io.PrintWriter
import scala.io.Source

class AccountManager {

  def readFromCSV: List[Account] = {
    
  }

  def writeToCSV(accList: List[Account]): Unit = {

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
  
  // Run this main method to manually test Account and AccountManager instances and functions
  // If readFromCSV is not implemented at time of test, a dummy return must be provided for it
  def main(args: Array[String]) = {
    val am1 = new AccountManager
    
  }
}