import Models.{BuyALetter, Consolation, Discount, Risk}
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import org.scalatestplus.play.PlaySpec
import services.GameService

class HangmanTest extends PlaySpec with GuiceOneAppPerSuite{
  val service = app.injector.instanceOf[GameService]
  val discount = app.injector.instanceOf[Discount]
  val risk = app.injector.instanceOf[Risk]
  val consolation = app.injector.instanceOf[Consolation]
  val buyALetter = app.injector.instanceOf[BuyALetter]
}
