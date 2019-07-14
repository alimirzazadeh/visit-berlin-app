package controllers

case class Review(title: String, body: String, authorEmail: String, associatedID: Int, rating: Int) {

  require(rating >= 1 && rating <= 10)

  def changeBody(newBody: String): Review = copy(body = newBody)

  def changeTitle(newTitle: String): Review = copy(title = newTitle)

  def changeRating(newRating: Int): Review = copy(rating = newRating)

  override def equals(other: Any): Boolean = {
    other match {
      case that: Review =>
        this.title == that.title && this.body == that.body &&
          this.authorEmail == that.authorEmail && this.associatedID == that.associatedID &&
          this.rating == that.rating
      case _ => false
    }
  }

  override def toString: String = {
    s"Attraction ID: $associatedID, Author Email: $authorEmail, Review Title: $title," +
      s"Review Body: $body, Review Score: $rating/10"
  }

  def toList: List[String] = {
    List(s"$title", s"$body", s"$authorEmail", s"$associatedID", s"$rating")
  }

}

object Review {
}
