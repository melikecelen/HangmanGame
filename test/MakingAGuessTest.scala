import Models.{GameState, MoveResult, Word}

import play.api.libs.json._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class MakingAGuessTest extends HangmanTest {
  val hangmanTest = new HangmanTest
  "GameController POST" should {

    "making a guess with used (non-usable) letter" in {
      val moves: ListBuffer[MoveResult] = new mutable.ListBuffer[MoveResult]
      moves += new MoveResult(Some(service.alphabet('b')), None, Some(true), "b********", 100, None, new GameState(false, "Continue"))
      service.createANewGameTest(1, 100, new Word("butterfly", "animal"), moves)
      val request = FakeRequest(POST, "/guess").withJsonBody(Json.obj("letter" -> "b", "cardName" -> JsNull, "position" -> JsNull)).withHeaders("Host" -> "localhost")
      val guess = route(app, request).get
      status(guess) mustBe OK
      contentType(guess) mustBe Some("application/json")
      contentAsJson(guess) mustBe Json.obj("status" -> "KO", "message" -> "This letter already choosed")
    }
    "making a guess with an unused (usable) letter" in {
      service.createANewGameTest(1, 100, new Word("butterfly", "animal"))
      val request = FakeRequest(POST, "/guess").withJsonBody(Json.obj("letter" -> "c", "cardName" -> JsNull, "position" -> JsNull)).withHeaders("Host" -> "localhost")
      val guess = route(app, request).get
      status(guess) mustBe OK
      contentType(guess) mustBe Some("application/json")
    }

    "The letter does not exist in the secret word" in {
      service.createANewGameTest(1, 100, new Word("butterfly", "animal"))
      val request = FakeRequest(POST, "/guess").withJsonBody(Json.obj("letter" -> "a", "cardName" -> JsNull, "position" -> JsNull)).withHeaders("Host" -> "localhost")
      val guess = route(app, request).get
      status(guess) mustBe OK
      contentType(guess) mustBe Some("application/json")
      (contentAsJson(guess) \ "message" \ "result").get mustEqual JsBoolean(false)
    }

    "The letter exists in secret word in one location" in {
      service.createANewGameTest(1, 100, new Word("butterfly", "animal"))
      val request = FakeRequest(POST, "/guess").withJsonBody(Json.obj("letter" -> "b", "cardName" -> JsNull, "position" -> JsNull)).withHeaders("Host" -> "localhost")
      val guess = route(app, request).get
      status(guess) mustBe OK
      contentType(guess) mustBe Some("application/json")
      (contentAsJson(guess) \ "message" \ "result").get mustEqual JsBoolean(true)
    }

    "The letter exists in secret word in multiple locations" in {
      service.createANewGameTest(1, 100, new Word("butterfly", "animal"))
      val request = FakeRequest(POST, "/guess").withJsonBody(Json.obj("letter" -> "t", "cardName" -> JsNull, "position" -> JsNull)).withHeaders("Host" -> "localhost")
      val guess = route(app, request).get
      status(guess) mustBe OK
      contentType(guess) mustBe Some("application/json")
      (contentAsJson(guess) \ "message" \ "secretWord").get mustEqual JsString("**tt*****")
    }
  }
}

