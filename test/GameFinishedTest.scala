import Models.{GameState, MoveResult, Word}
import play.api.libs.json._
import play.api.test.FakeRequest
import play.api.test.Helpers._

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class GameFinishedTest extends HangmanTest {
 "The application finishes by revealing the secret word completely" in {
    val moves: ListBuffer[MoveResult] = new mutable.ListBuffer[MoveResult]
    moves += new MoveResult(Some(service.alphabet('b')), None, Some(true), "butterfl*", 10, None, new GameState(false, "Continue"))
    val word:Word =new Word("butterfly", "animal")
    for(i<-0 until word.visibility.length-1)
      word.visibility(i)=true
    service.createANewGameTest(1, 10, word, moves)
    val request = FakeRequest(POST, "/guess").withJsonBody(Json.obj("letter" -> "y", "cardName" -> JsNull, "position" -> JsNull)).withHeaders("Host" -> "localhost")
    val guess = route(app, request).get
    contentType(guess) mustBe Some("application/json")
    (contentAsJson(guess) \ "message" \ "result").get mustEqual JsBoolean(true)
    (contentAsJson(guess) \ "message" \ "gameState" \ "finished").get mustEqual JsBoolean(true)
    (contentAsJson(guess) \ "message" \ "gameState" \ "message").get mustEqual JsString("You won")

  }
  "The application finishes by user’s not having enough points to make a new guess" in {
    val moves: ListBuffer[MoveResult] = new mutable.ListBuffer[MoveResult]
    moves += new MoveResult(Some(service.alphabet('b')), None, Some(true), "butterfl*", 4, None, new GameState(false, "Continue"))
    val word:Word =new Word("butterfly", "animal")
    for(i<-0 until word.visibility.length-1)
      word.visibility(i)=true
    service.createANewGameTest(1, 4, word, moves)
    val request = FakeRequest(POST, "/guess").withJsonBody(Json.obj("letter" -> "z", "cardName" -> JsNull, "position" -> JsNull)).withHeaders("Host" -> "localhost")
    val guess = route(app, request).get
    contentType(guess) mustBe Some("application/json")
    (contentAsJson(guess) \ "message" \ "result").get mustEqual JsBoolean(false)
    (contentAsJson(guess) \ "message" \ "gameState" \ "finished").get mustEqual JsBoolean(true)
    (contentAsJson(guess) \ "message" \ "gameState" \ "message").get mustEqual JsString("You lost")
  }
}
