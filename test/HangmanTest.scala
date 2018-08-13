import Models.{Card, MoveResult}
import controllers.HomeController
import org.scalatestplus.play.{OneAppPerTest, PlaySpec}
import play.api.inject.guice.GuiceApplicationBuilder
import services.GameService

import scala.collection.mutable.ListBuffer

class HangmanTest extends PlaySpec with OneAppPerTest {
  val controller = app.injector.instanceOf[HomeController]
  val service = app.injector.instanceOf[GameService]

  def createAGame(level: Int, point: Int, moves: Option[ListBuffer[MoveResult]], usedCards: Option[ListBuffer[Card]]): Unit = {
    val game = service.createANewGame(level, point)
  }
}
