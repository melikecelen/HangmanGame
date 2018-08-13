import controllers.{HomeController}
import org.scalatestplus.play._
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.{Play}
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.{JsNull, Json}
import play.api.test.FakeRequest


class deneme extends PlaySpec with GuiceOneAppPerSuite{
}


import org.scalatest._
import org.scalatestplus.play._

import play.api.test._
import play.api.test.Helpers._

class ExampleSpec extends PlaySpec with GuiceOneAppPerSuite {

  // Override fakeApplication if you need a Application with other than
  // default parameters.
  override def fakeApplication() = new GuiceApplicationBuilder().configure(Map("ehcacheplugin" -> "disabled")).build()

  "The GuiceOneAppPerSuite trait" must {
    "provide an Application" in {
      app.configuration.getOptional[String]("ehcacheplugin") mustBe Some("disabled")
    }
    "start the Application" in {
      Play.maybeApplication mustBe Some(app)
    }
  }

 // val a = route(app, REQUESY)
}


class HomeControllerSpec extends PlaySpec with OneAppPerTest {

  "HomeController GET" should {

    /*"render the index page from a new instance of controller" in {
      val controller = new GameController
      val home = controller.index().apply(FakeRequest())

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Welcome to Play")
    }*/

   /* "render the index page from the application" in {
      val controller = app.injector.instanceOf[HomeController]
      val home = controller.index().apply(FakeRequest())

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Welcome to Play")
    }

    "render the index page from the router" in {
      // Need to specify Host header to get through AllowedHostsFilter
      val request = FakeRequest(GET, "/").withHeaders("Host" -> "localhost")
      val home = route(app, request).get

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Welcome to Play")
    }*/

    "make a new guess with letter" in{
      val controller = app.injector.instanceOf[HomeController]
      val request1 = FakeRequest(POST,"/createANewGame").withHeaders("Host"-> "localhost").withJsonBody(Json.obj("level" -> 1))
      val home = route(app,request1).get
      println(contentAsJson(home))

      //Thread.sleep(1000)

      val request = FakeRequest(POST, "/guess").withJsonBody(Json.obj("letter" -> "m","cardName" -> JsNull,"position" -> JsNull)).withHeaders("Host" -> "localhost")
      val a = route(app,request).get

      status(a) mustBe OK
      contentType(a) mustBe Some("application/json")
    }
  }
}
//'{"letter": "m","cardName":null,"position":null}'


/*
import org.scalatestplus.play._
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Configuration
import play.api.inject.guice.GuiceApplicationBuilder

class MakingAGuessTest extends PlaySpec with GuiceOneAppPerSuite{
  val application = new GuiceApplicationBuilder()
    .configure(Configuration("a" -> 1))
    .configure(Map("b" -> 2, "c" -> "three"))
    .configure("d" -> 4, "e" -> "five")
    .build()
}*/