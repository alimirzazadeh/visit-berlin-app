package controllers

case class Review(title: String, body: String, authorEmail: String, associatedID: Int, score: Int) {

  require(score >= 1 && score <= 10)

  def changeBody(newBody: String): Review = copy(body = newBody)

  def changeTitle(newTitle: String): Review = copy(title = newTitle)

  def changeScore(newScore: Int): Review = copy(score = newScore)

  override def equals(other: Any): Boolean = {
    other match {
      case that: Review =>
        this.title == that.title && this.body == that.body &&
          this.authorEmail == that.authorEmail && this.associatedID == that.associatedID &&
          this.score == that.score
      case _ => false
    }
  }

  override def toString: String = {
    s"Attraction ID: $associatedID, Author Email: $authorEmail, Review Title: $title," +
      s"Review Body: $body, Review Score: $score/10"
  }

  def toList: List[String] = {
    List(s"$title", s"$body", s"$authorEmail", s"$associatedID", s"$score")
  }

}

object Review {
}
