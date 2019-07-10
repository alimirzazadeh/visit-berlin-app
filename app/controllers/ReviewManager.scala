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
        createList(Review(info(0), info(1), info(2), info(3).toInt) :: reviewList, csvInfo)
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
    writer.write("attractionID^authorEmail^title^body\n")
    for (review <- reviewList) {
      val currentReview = review.toList
      for (item <- 0 to 2) writer.write(currentReview(item).replaceAll("\\s", " ") + "^")
      writer.write(currentReview(3) + '\n')
    }
    writer.close()
  }

}

object ReviewManager {
  val filename: String = "public/reviews.csv"
}
