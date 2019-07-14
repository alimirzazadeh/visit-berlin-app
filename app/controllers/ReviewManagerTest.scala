package controllers

import org.scalatest.FlatSpec

class ReviewManagerTest extends FlatSpec {

  "A Review Manager" should "remove a review from the csv" in {
    val rm = new ReviewManager()
    assert(List(new Review("interesting", "review", "this@gmail.com", 5, 1))
      == rm.removeReview(new Review("test", "remove", "remove@gmail.com", 1, 1)))
  }

  it should "add a review to the csv" in {
    val rm = new ReviewManager()
    assert(List(new Review("add", "review", "add@gmail.com", 1, 2),
      new Review("test", "remove", "remove@gmail.com", 1, 1),
      new Review("interesting", "review", "this@gmail.com", 5, 1))
      == rm.addReview(new Review("add", "review", "add@gmail.com", 1, 2)))
  }

  it should "edit a review" in {
    val rm = new ReviewManager()
    assert(List(new Review("edited", "review", "remove@gmail.com", 1, 1),
      new Review("interesting", "review", "this@gmail.com", 5, 1))
      == rm.editReview(new Review("test", "remove", "remove@gmail.com", 1, 1),
      new Review("edited", "review", "remove@gmail.com", 1, 1)))
  }

  it should "determine if a review exists" in {
    val rm = new ReviewManager()
    assert(true == rm.findReview(rm.readFromCSV, new Review("interesting", "review", "this@gmail.com", 5, 1)))
    assert(false == rm.findReview(rm.readFromCSV, new Review("this", "review", "doesntexist@gmail.com", 4, 1)))
  }


}
