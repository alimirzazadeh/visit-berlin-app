package controllers

import java.io.{File, PrintWriter}

import scala.io.Source

class ReviewManager {

  def readFromCSV: List[Review] = {
    def createList(reviews: List[Review], csvInfo: Iterator[String]): List[Review] = {
      if (csvInfo.hasNext) {
        val info = csvInfo.next().split('^')
        /*Different indexes in the info list are specific pieces of information in the csv file.
         *This allows the csv file to make sense when looked at by a human, as everything is in order
         */
        createList(Review(info(0), info(1), info(2), info(3).toInt, info(4).toInt) :: reviews, csvInfo)
      }
      else
        reviews
    }
    val file = new File(ReviewManager.filename)
    if (file.exists())
      createList(List(), Source.fromFile(ReviewManager.filename).getLines.drop(1))
    else {
      writeToCSV(List())
      List()
    }
  }

  def writeToCSV(reviews: List[Review]): Unit = {
    val writer = new PrintWriter(new File(ReviewManager.filename))
    writer.write("title^body^authorEmail^attractionID^rating\n")
    for (review <- reviews) {
      val currentReview = review.toList
      for (item <- 0 to 3) writer.write(currentReview(item).replaceAll("\\s", " ") + "^")
      writer.write(currentReview(4) + '\n')
    }
    writer.close()
  }

  def addReview(review: Review): List[Review] = {
    val reviews = readFromCSV
    val updatedReviews = if (findReview(reviews, review)) reviews else review :: reviews
    updatedReviews
  }

  def findReview(reviews: List[Review], review: Review): Boolean = {
    reviews.exists(_.title == review.title)
  }

  def reviewFromTitle(title: String): Review = {
    val foundReviews = readFromCSV.filter(_.title == title)
    if (foundReviews.nonEmpty) foundReviews.head
    else null
  }

  def removeReview(review: Review): List[Review] = {
    val reviews = readFromCSV
    val updatedReviews = if (!findReview(reviews, review)) reviews else reviews.filter(_.title != review.title)
    updatedReviews
  }

  def editReview(oldReview: Review, newReview: Review): List[Review] = {
    val reviews = readFromCSV
    val updatedReviews = if (!findReview(reviews, oldReview)) reviews else newReview :: reviews.filter(_.title != oldReview.title)
    updatedReviews
  }

  def supplyReviews(attractionID: Int): List[Review] = {
    val reviews = readFromCSV
    val updatedReviews = reviews.filter(_.associatedID == attractionID)
    updatedReviews
  }

  def averageReviewScore(attractionID: Int): Double = {
    val specificReviews = supplyReviews(attractionID)
    val averageScore = specificReviews.reduce((r1, r2) => r1.score + r2.score) / specificReviews.length
    averageScore
  }
}

object ReviewManager {
  val filename: String = "public/reviews.csv"

  def main(args: Array[String]): Unit = {
    val rm = new ReviewManager

  }
}
