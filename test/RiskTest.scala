import Models._
import play.api.libs.json._
import play.api.test.FakeRequest
import play.api.test.Helpers._

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class RiskTest extends HangmanTest {
  "GameController POST" should {
    "Making a guess with risk card quota (used 2 times) available" in {
      service.createANewGameTest(1, 49, new Word("butterfly", "animal"))
      val request = FakeRequest(POST, "/guess").withJsonBody(Json.obj("letter" -> "u", "cardName" -> "Risk", "position" -> JsNull)).withHeaders("Host" -> "localhost")
      val guess = route(app, request).get
      status(guess) mustBe OK
      contentType(guess) mustBe Some("application/json")
      (contentAsJson(guess) \ "message" \ "card").get mustEqual JsString("Risk")
      (contentAsJson(guess) \ "message" \ "point").get mustEqual Json.toJson(49-risk.cost)
    }
    "Making a guess with discount card quota unavailable" in {
      val usedCards:ListBuffer[Card]=ListBuffer[Card](risk,risk)
      service.createANewGameTest(1, 39, new Word("butterfly", "animal"),usedCards = usedCards)
      val request = FakeRequest(POST, "/guess").withJsonBody(Json.obj("letter" -> "c", "cardName" -> "Risk", "position" -> JsNull)).withHeaders("Host" -> "localhost")
      val guess = route(app, request).get
      status(guess) mustBe OK
      contentType(guess) mustBe Some("application/json")
      contentAsJson(guess) mustBe Json.obj("status" -> "KO", "message" -> "Card isn't available or affordable! Make a new guess")
    }

    "The user has has enough points to use the risk card and pick the letter" in {
      service.createANewGameTest(1, 35, new Word("butterfly", "animal"))
      val request = FakeRequest(POST, "/guess").withJsonBody(Json.obj("letter" -> "f", "cardName" -> "Risk", "position" -> JsNull)).withHeaders("Host" -> "localhost")
      val guess = route(app, request).get
      status(guess) mustBe OK
      contentType(guess) mustBe Some("application/json")
      (contentAsJson(guess) \ "message" \ "card").get mustEqual JsString("Risk")
      (contentAsJson(guess) \ "message" \ "point").get mustEqual Json.toJson(35-risk.cost)
    }

    "The user does not have enough points to use the risk card and pick the letter" in {
      service.createANewGameTest(1, 20, new Word("butterfly", "animal"))
      val request = FakeRequest(POST, "/guess").withJsonBody(Json.obj("letter" -> "c", "cardName" -> "Risk", "position" -> JsNull)).withHeaders("Host" -> "localhost")
      val guess = route(app, request).get
      status(guess) mustBe OK
      contentType(guess) mustBe Some("application/json")
      contentAsJson(guess) mustBe Json.obj("status" -> "KO", "message" -> "Card isn't available or affordable! Make a new guess")
    }
    "The letter does not exist in the secret word. (The next guess will deduct points from the player.)" in {
      val moves: ListBuffer[MoveResult] = new mutable.ListBuffer[MoveResult]
      moves += new MoveResult(Some(service.alphabet('a')), Some("Risk"), Some(false), "*********", 40, None, new GameState(false, "Continue"))
      service.createANewGameTest(1, 40, new Word("butterfly", "animal"), moves)
      val request = FakeRequest(POST, "/guess").withJsonBody(Json.obj("letter" -> "c", "cardName" -> JsNull, "position" -> JsNull)).withHeaders("Host" -> "localhost")
      val guess = route(app, request).get
      status(guess) mustBe OK
      contentType(guess) mustBe Some("application/json")
      (contentAsJson(guess) \ "message" \ "result").get mustEqual JsBoolean(false)
      (contentAsJson(guess) \ "message" \ "point").get mustEqual Json.toJson(40-Some(service.alphabet('c')).get.cost)
    }

    "The letter exists in secret word in one location. (The next guess must not deduct any points from the player.)" in{
      val moves: ListBuffer[MoveResult] = new mutable.ListBuffer[MoveResult]
      moves += new MoveResult(Some(service.alphabet('b')), Some("Risk"), Some(true), "b********", 40, None, new GameState(false, "Continue"))
      service.createANewGameTest(1, 40, new Word("butterfly", "animal"), moves)
      val request = FakeRequest(POST, "/guess").withJsonBody(Json.obj("letter" -> "c", "cardName" -> JsNull, "position" -> JsNull)).withHeaders("Host" -> "localhost")
      val guess = route(app, request).get
      status(guess) mustBe OK
      contentType(guess) mustBe Some("application/json")
      (contentAsJson(guess) \ "message" \ "result").get mustEqual JsBoolean(false)
      (contentAsJson(guess) \ "message" \ "point").get mustEqual Json.toJson(40)
    }
  }
}


/*
*/