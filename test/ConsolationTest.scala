import Models._
import play.api.libs.json._
import play.api.test.FakeRequest
import play.api.test.Helpers._

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class ConsolationTest extends HangmanTest {
  "Making a guess with consolation card quota (used 1 time) available" in {
    service.createANewGameTest(1, 49, new Word("butterfly", "animal"))
    val request = FakeRequest(POST, "/guess").withJsonBody(Json.obj("letter" -> "u", "cardName" -> "Consolation", "position" -> JsNull)).withHeaders("Host" -> "localhost")
    val guess = route(app, request).get
    status(guess) mustBe OK
    contentType(guess) mustBe Some("application/json")
    (contentAsJson(guess) \ "message" \ "card").get mustEqual JsString("Consolation")
    (contentAsJson(guess) \ "message" \ "point").get mustEqual Json.toJson(49 - consolation.cost)
  }
  "Making a guess with consolation card quota unavailable" in {
    val usedCards: ListBuffer[Card] = ListBuffer[Card](consolation)
    service.createANewGameTest(1, 39, new Word("butterfly", "animal"), usedCards = usedCards)
    val request = FakeRequest(POST, "/guess").withJsonBody(Json.obj("letter" -> "c", "cardName" -> "Consolation", "position" -> JsNull)).withHeaders("Host" -> "localhost")
    val guess = route(app, request).get
    status(guess) mustBe OK
    contentType(guess) mustBe Some("application/json")
    contentAsJson(guess) mustBe Json.obj("status" -> "KO", "message" -> "Card isn't available or affordable! Make a new guess")
  }

  "The user has has enough points to use the consolation card and pick the letter" in {
    service.createANewGameTest(1, 35, new Word("butterfly", "animal"))
    val request = FakeRequest(POST, "/guess").withJsonBody(Json.obj("letter" -> "f", "cardName" -> "Consolation", "position" -> JsNull)).withHeaders("Host" -> "localhost")
    val guess = route(app, request).get
    status(guess) mustBe OK
    contentType(guess) mustBe Some("application/json")
    (contentAsJson(guess) \ "message" \ "card").get mustEqual JsString("Consolation")
    (contentAsJson(guess) \ "message" \ "point").get mustEqual Json.toJson(35 - consolation.cost)
  }

  "The user does not have enough points to use the consolation card and pick the letter" in {
    service.createANewGameTest(1, 4, new Word("butterfly", "animal"))
    val request = FakeRequest(POST, "/guess").withJsonBody(Json.obj("letter" -> "c", "cardName" -> "Consolation", "position" -> JsNull)).withHeaders("Host" -> "localhost")
    val guess = route(app, request).get
    status(guess) mustBe OK
    contentType(guess) mustBe Some("application/json")
    contentAsJson(guess) mustBe Json.obj("status" -> "KO", "message" -> "Card isn't available or affordable! Make a new guess")
  }

  "The letter does not exist in the secret word. (The cost of next guess will be deducted %50 and rounded down)" in {
    val moves: ListBuffer[MoveResult] = new mutable.ListBuffer[MoveResult]
    moves += new MoveResult(Some(service.alphabet('a')), Some("Consolation"), Some(false), "*********", 40, None, new GameState(false, "Continue"))
    service.createANewGameTest(1, 40, new Word("butterfly", "animal"), moves)
    val request = FakeRequest(POST, "/guess").withJsonBody(Json.obj("letter" -> "c", "cardName" -> JsNull, "position" -> JsNull)).withHeaders("Host" -> "localhost")
    val guess = route(app, request).get
    status(guess) mustBe OK
    contentType(guess) mustBe Some("application/json")
    (contentAsJson(guess) \ "message" \ "result").get mustEqual JsBoolean(false)
    (contentAsJson(guess) \ "message" \ "point").get mustEqual Json.toJson(40 - Some(service.alphabet('c')).get.cost/2)
  }

  "The letter exists in secret word in one location. (The cost of next guess will not change.)" in {
    val moves: ListBuffer[MoveResult] = new mutable.ListBuffer[MoveResult]
    moves += new MoveResult(Some(service.alphabet('b')), Some("Consolation"), Some(true), "b********", 40, None, new GameState(false, "Continue"))
    service.createANewGameTest(1, 40, new Word("butterfly", "animal"), moves)
    val request = FakeRequest(POST, "/guess").withJsonBody(Json.obj("letter" -> "c", "cardName" -> JsNull, "position" -> JsNull)).withHeaders("Host" -> "localhost")
    val guess = route(app, request).get
    status(guess) mustBe OK
    contentType(guess) mustBe Some("application/json")
    (contentAsJson(guess) \ "message" \ "result").get mustEqual JsBoolean(false)
    (contentAsJson(guess) \ "message" \ "point").get mustEqual Json.toJson(40 -Some(service.alphabet('c')).get.cost)
  }
}


