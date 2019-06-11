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
    val updatedList = if (!accList.contains(acc)) accList else accList.filter(_ == acc)
    updatedList
  }

  def editAccount(oldAcc: Account, newAcc: Account): List[Account] = {
    val accList = readFromCSV
    val updatedList = if (!accList.contains(oldAcc)) accList else newAcc :: accList.filter(_ == oldAcc)
    updatedList
  }
}
