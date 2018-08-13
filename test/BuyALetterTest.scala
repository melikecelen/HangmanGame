import Models.{Card, Word}
import play.api.libs.json._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import scala.collection.mutable.ListBuffer

class BuyALetterTest extends HangmanTest {
  "The player buys a letter with buying quota available (1 time)" in {
    service.createANewGameTest(1, 100, new Word("butterfly", "animal"))
    val request = FakeRequest(POST, "/guess").withJsonBody(Json.obj("letter" -> JsNull, "cardName" -> "Buy A Letter", "position" -> JsNumber(1))).withHeaders("Host" -> "localhost")
    val guess = route(app, request).get
    status(guess) mustBe OK
    contentType(guess) mustBe Some("application/json")
    (contentAsJson(guess) \ "message" \ "card").get mustEqual JsString("Buy A Letter")
    (contentAsJson(guess) \ "message" \ "point").get mustEqual Json.toJson(100-buyALetter.cost)
  }
  "The player buys a letter with buying quota unavailable" in {
    val usedCards: ListBuffer[Card] = ListBuffer[Card](buyALetter)
    service.createANewGameTest(1, 80, new Word("butterfly", "animal"), usedCards = usedCards)
    val request = FakeRequest(POST, "/guess").withJsonBody(Json.obj("letter" -> JsNull, "cardName" -> "Buy A Letter", "position" -> JsNumber(2))).withHeaders("Host" -> "localhost")
    val guess = route(app, request).get
    status(guess) mustBe OK
    contentType(guess) mustBe Some("application/json")
    contentAsJson(guess) mustBe Json.obj("status" -> "KO", "message" -> "Card isn't available or affordable! Make a new guess")
  }

  "The player buys a letter with enough points available" in {
    service.createANewGameTest(1, 100, new Word("butterfly", "animal"))
    val request = FakeRequest(POST, "/guess").withJsonBody(Json.obj("letter" -> JsNull, "cardName" -> "Buy A Letter", "position" -> JsNumber(0))).withHeaders("Host" -> "localhost")
    val guess = route(app, request).get
    status(guess) mustBe OK
    contentType(guess) mustBe Some("application/json")
    (contentAsJson(guess) \ "message" \ "card").get mustEqual JsString("Buy A Letter")
    (contentAsJson(guess) \ "message" \ "point").get mustEqual Json.toJson(100 - buyALetter.cost)
  }

  "The player buys a letter without enough points available" in {
    service.createANewGameTest(1, 10, new Word("butterfly", "animal"))
    val request = FakeRequest(POST, "/guess").withJsonBody(Json.obj("letter" -> JsNull, "cardName" -> "Buy A Letter", "position" -> JsNumber(0))).withHeaders("Host" -> "localhost")
    val guess = route(app, request).get
    status(guess) mustBe OK
    contentType(guess) mustBe Some("application/json")
    contentAsJson(guess) mustBe Json.obj("status" -> "KO", "message" -> "Card isn't available or affordable! Make a new guess")
  }
}
