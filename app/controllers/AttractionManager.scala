package controllers

import java.io.{File, PrintWriter}
import scala.io.Source

class AttractionManager {

  def readFromCSV: List[Attraction] = {
    def createList(attractionList: List[Attraction], csvInfo: Iterator[String]): List[Attraction] = {
      if (csvInfo.hasNext) {
        val info = csvInfo.next().split('^')
        /*Different indexes in the info list are specific pieces of information in the csv file.
         *This allows the csv file to make sense when looked at by a human, as everything is in order
         */
        createList(Attraction(info(0), info(1), info(2), info(3)) :: attractionList, csvInfo)
      }
      else
        attractionList
    }
    val file = new File(AttractionManager.filename)
    if (file.exists())
      createList(List(), Source.fromFile(AttractionManager.filename).getLines.drop(1))
    else {
      writeToCSV(List())
      List()
    }
  }

  /*
  -change it to ^ separated
  -get rid of new lines

   */

  def writeToCSV(attractionList: List[Attraction]): Unit = {
    val writer = new PrintWriter(new File(AttractionManager.filename))
    writer.write("name^pictureURL^description^location\n")
    for (attraction <- attractionList) {
      val currentAttraction = attraction.toList
      for (item <- 0 to 2) writer.write(currentAttraction(item).replaceAll("\\s", " ") + "^")
      writer.write(currentAttraction(3) + '\n')
    }
    writer.close()
  }

  def addAttraction(attraction: Attraction): List[Attraction] = {
    val attractionList = readFromCSV
    val updatedList = if (findAttraction(attractionList, attraction)) attractionList else attraction :: attractionList
    updatedList
  }

  def removeAttraction(attraction: Attraction): List[Attraction] = {
    val attractionList = readFromCSV
    val updatedList = if (!findAttraction(attractionList, attraction)) attractionList else attractionList.filter(_.name != attraction.name)
    updatedList
  }

  def editAttraction(oldAttraction: Attraction, newAttraction: Attraction): List[Attraction] = {
    val attractionList = readFromCSV
    val updatedList = if (!findAttraction(attractionList, oldAttraction)) attractionList else newAttraction :: attractionList.filter(_.name != oldAttraction.name)
    updatedList
  }

  def findAttraction(attractionList: List[Attraction], attraction: Attraction): Boolean = {
    attractionList.exists(_.name == attraction.name)
  }

  def attractionFromName(name: String): Attraction = {
    val foundAttractions = readFromCSV.filter(_.name == name)
    if (foundAttractions.nonEmpty) foundAttractions.head
    else null
  }


}

object AttractionManager {

  val filename: String = "public/attractions.csv"

  def main(args: Array[String]): Unit = {
    val am = new AttractionManager()
    am.addAttraction(new Attraction("test", "test", "test", "test"))
  }

}
