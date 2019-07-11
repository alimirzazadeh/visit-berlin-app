package controllers

case class Review(title: String, body: String, authorEmail: String, rating: Int, attractionID: Int) {


  //give attractions id numbers
  //make csv manager for reviews
  //id,authorEmail,title,body

  def changeBody(newBody: String): Review = copy(body = newBody)

  def changeTitle(newTitle: String): Review = copy(title = newTitle)

  override def equals(other: Any): Boolean = {
    other match {
      case that: Review =>
        this.title == that.title && this.body == that.body &&
          this.authorEmail == that.authorEmail && this.attractionID == that.attractionID
      case _ => false
    }
  }

  override def toString: String = {
    s"Attraction ID: $attractionID, Author Email: $authorEmail, Review Title: $title, Review Body: $body"
  }

  def toList: List[String] = {
    List(s"$attractionID", s"$authorEmail", s"$title", s"$body")
  }

}

object Review {

}
