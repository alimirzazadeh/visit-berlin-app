package controllers

import java.io.{File, PrintWriter}

import scala.io.Source

class ReviewManager {


  def readFromCSV: List[Review] = {
    def createList(reviewList: List[Review], csvInfo: Iterator[String]): List[Review] = {
      if (csvInfo.hasNext) {
        val info = csvInfo.next().split('^')
        /*Different indexes in the info list are specific pieces of information in the csv file.
         *This allows the csv file to make sense when looked at by a human, as everything is in order
         */
        createList(Review(info(0), info(1), info(2), info(3).toInt, info(4).toInt) :: reviewList, csvInfo)
      }
      else
        reviewList
    }
    val file = new File(ReviewManager.filename)
    if (file.exists())
      createList(List(), Source.fromFile(ReviewManager.filename).getLines.drop(1))
    else {
      writeToCSV(List())
      List()
    }
  }

  def writeToCSV(reviewList: List[Review]): Unit = {
    val writer = new PrintWriter(new File(AttractionManager.filename))
    writer.write("title^body^authorEmail^attractionID^rating\n")
    for (review <- reviewList) {
      val currentReview = review.toList
      for (item <- 0 to 3) writer.write(currentReview(item).replaceAll("\\s", " ") + "^")
      writer.write(currentReview(4) + '\n')
    }
    writer.close()
  }

  def addReview(review: Review): List[Review] = {
    val reviewList = readFromCSV
    val updatedList = if (findReview(reviewList, review)) reviewList else review :: reviewList
    updatedList
  }

  def findReview(reviewList: List[Review], review: Review): Boolean = {
    reviewList.exists(_.title == review.title)
  }

  def removeReview(review: Review): List[Review] = {
    val reviewList = readFromCSV
    val updatedList = if (!findReview(reviewList, review)) reviewList else reviewList.filter(_.title != review.title)
    updatedList
  }

  def editReview(oldReview: Review, newReview: Review): List[Review] = {
    val reviewList = readFromCSV
    val updatedList = if (!findReview(reviewList, oldReview)) reviewList else newReview :: reviewList.filter(_.title != oldReview.title)
    updatedList
  }



}

object ReviewManager {
  val filename: String = "public/reviews.csv"

  def main(args: Array[String]): Unit = {
    val rm = new ReviewManager

  }
}
