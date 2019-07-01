package controllers

import java.awt.Image

case class Attraction(name: String, pictureURL: String, description: String, location: String) {

  def changeName(newName: String): Attraction = copy(name = newName)

  def changePictureURL(newPictureURL: String): Attraction = copy(pictureURL = newPictureURL)

  def changeDescription(newDescription: String): Attraction = copy(description = newDescription)

  def changeLocation(newLocation: String): Attraction = copy(location = newLocation)

  override def equals(other: Any): Boolean = {
    other match {
      case that: Attraction =>
        this.name == that.name && this.pictureURL == that.pictureURL &&
          this.description == that.description && this.location == that.location
      case _ => false
    }
  }

  override def toString: String = {
    s"Name: $name, Picture Address: $pictureURL, Description: $description, Location: $location"
  }

  def toList: List[String] = {
    List(s"$name", s"$pictureURL", s"$description", s"$location")
  }
}

object Attraction {

}