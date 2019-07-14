package controllers

case class Review(title: String, body: String, authorEmail: String, rating: Int, attractionID: Int) {

  require(rating >= 1 && rating <= 10)

  def changeBody(newBody: String): Review = copy(body = newBody)

  def changeTitle(newTitle: String): Review = copy(title = newTitle)

  def changeRating(newRating: Int): Review = copy(rating = newRating)

  override def equals(other: Any): Boolean = {
    other match {
      case that: Review =>
        this.title == that.title && this.body == that.body &&
          this.authorEmail == that.authorEmail && this.attractionID == that.attractionID &&
          this.rating == that.rating
      case _ => false
    }
  }

  override def toString: String = {
    s"Attraction ID: $attractionID, Author Email: $authorEmail, Review Title: $title," +
      s"Review Body: $body, Review Score: $rating/10"
  }

  def toList: List[String] = {
    List(s"$attractionID", s"$authorEmail", s"$title", s"$body", s"$rating")
  }

}

object Review {

}
