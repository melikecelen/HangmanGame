import play.api.libs.json.{JsNull, Json}
import play.api.test.{FakeRequest}
import play.api.test.Helpers._

class MakingAGuessTest extends HangmanTest {
  "GameController POST" should {
    "make a new guess with letter" in {
      //val controller = app.injector.instanceOf[HomeController]
      val request1 = FakeRequest(POST, "/createANewGame").withHeaders("Host" -> "localhost").withJsonBody(Json.obj("level" -> 1))
      val home = route(app, request1).get
      println(contentAsJson(home))

      //Thread.sleep(1000)

      val request = FakeRequest(POST, "/guess").withJsonBody(Json.obj("letter" -> "m", "cardName" -> JsNull, "position" -> JsNull)).withHeaders("Host" -> "localhost")
      val a = route(app, request).get

      status(a) mustBe OK
      contentType(a) mustBe Some("application/json")
    }
    "make a new guess with used letter" in {
      val request = FakeRequest(POST, "/guess").withJsonBody(Json.obj("letter"->"a","cardName"-> JsNull,"position"-> JsNull)).withHeaders("Host" -> "localhost")
    }
  }
}
