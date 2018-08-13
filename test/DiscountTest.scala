import Models.{Card, Word}
import play.api.libs.json._
import play.api.test.FakeRequest
import play.api.test.Helpers._

import scala.collection.mutable.ListBuffer

class DiscountTest extends HangmanTest {
  "GameController POST" should {
    "Making a guess with discount card quota (used 2 times) available" in{
      service.createANewGameTest(1, 39, new Word("butterfly", "animal"))
      val request = FakeRequest(POST, "/guess").withJsonBody(Json.obj("letter" -> "c", "cardName" -> "Discount", "position" -> JsNull)).withHeaders("Host" -> "localhost")
      val guess = route(app, request).get
      status(guess) mustBe OK
      contentType(guess) mustBe Some("application/json")
      (contentAsJson(guess) \ "message" \ "card").get mustEqual JsString("Discount")
      (contentAsJson(guess) \ "message" \ "point").get mustEqual Json.toJson(31)
    }

    "Making a guess with discount card quota unavailable" in{
      val usedCards:ListBuffer[Card]=ListBuffer[Card](discount,discount)
      service.createANewGameTest(1, 39, new Word("butterfly", "animal"),usedCards = usedCards)
      val request = FakeRequest(POST, "/guess").withJsonBody(Json.obj("letter" -> "c", "cardName" -> "Discount", "position" -> JsNull)).withHeaders("Host" -> "localhost")
      val guess = route(app, request).get
      status(guess) mustBe OK
      contentType(guess) mustBe Some("application/json")
      contentAsJson(guess) mustBe Json.obj("status" -> "KO", "message" -> "Card isn't available or affordable! Make a new guess")
    }

    "The user has has enough points to use the discount card and pick the letter" in{
      service.createANewGameTest(1, 35, new Word("butterfly", "animal"))
      val request = FakeRequest(POST, "/guess").withJsonBody(Json.obj("letter" -> "a", "cardName" -> "Discount", "position" -> JsNull)).withHeaders("Host" -> "localhost")
      val guess = route(app, request).get
      status(guess) mustBe OK
      contentType(guess) mustBe Some("application/json")
      (contentAsJson(guess) \ "message" \ "card").get mustEqual JsString("Discount")
      (contentAsJson(guess) \ "message" \ "point").get mustEqual Json.toJson(26)
    }
    "The user does not have enough points to use the discount card and pick the letter" in{
      service.createANewGameTest(1, 40, new Word("butterfly", "animal"))
      val request = FakeRequest(POST, "/guess").withJsonBody(Json.obj("letter" -> "c", "cardName" -> "Discount", "position" -> JsNull)).withHeaders("Host" -> "localhost")
      val guess = route(app, request).get
      status(guess) mustBe OK
      contentType(guess) mustBe Some("application/json")
      contentAsJson(guess) mustBe Json.obj("status" -> "KO", "message" -> "Card isn't available or affordable! Make a new guess")
    }
    "The letter does not exist in the secret word" in {
      service.createANewGameTest(1, 35, new Word("butterfly", "animal"))
      val request = FakeRequest(POST, "/guess").withJsonBody(Json.obj("letter" -> "a", "cardName" -> "Discount", "position" -> JsNull)).withHeaders("Host" -> "localhost")
      val guess = route(app, request).get
      status(guess) mustBe OK
      contentType(guess) mustBe Some("application/json")
      (contentAsJson(guess) \ "message" \ "result").get mustEqual JsBoolean(false)
      (contentAsJson(guess) \ "message" \ "point").get mustEqual Json.toJson(26)
    }

    "The letter exists in secret word in one location" in{
      service.createANewGameTest(1, 35, new Word("butterfly", "animal"))
      val request = FakeRequest(POST, "/guess").withJsonBody(Json.obj("letter" -> "y", "cardName" -> "Discount", "position" -> JsNull)).withHeaders("Host" -> "localhost")
      val guess = route(app, request).get
      status(guess) mustBe OK
      contentType(guess) mustBe Some("application/json")
      (contentAsJson(guess) \ "message" \ "result").get mustEqual JsBoolean(true)
      (contentAsJson(guess) \ "message" \ "point").get mustEqual Json.toJson(30)
      (contentAsJson(guess) \ "message" \ "secretWord").get mustEqual JsString("********y")
    }

    "The letter exists in secret word in multiple locations" in{
      service.createANewGameTest(1, 35, new Word("butterfly", "animal"))
      val request = FakeRequest(POST, "/guess").withJsonBody(Json.obj("letter" -> "t", "cardName" -> "Discount", "position" -> JsNull)).withHeaders("Host" -> "localhost")
      val guess = route(app, request).get
      status(guess) mustBe OK
      contentType(guess) mustBe Some("application/json")
      (contentAsJson(guess) \ "message" \ "result").get mustEqual JsBoolean(true)
      (contentAsJson(guess) \ "message" \ "point").get mustEqual Json.toJson(30)
      (contentAsJson(guess) \ "message" \ "secretWord").get mustEqual JsString("**tt*****")
    }
  }
}
