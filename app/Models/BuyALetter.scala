package Models

import javax.inject.{Inject, Singleton}
import play.api.Configuration
@Singleton
class BuyALetter @Inject()(configuration: Configuration) extends Card {
  val name: String = configuration.underlying.getString("cards.BuyALetter.name")
  val cost: Int = configuration.underlying.getInt("cards.BuyALetter.cost")
  val availableCount: Int = configuration.underlying.getInt("cards.BuyALetter.availableCount")

  override def isCardAffordable(point: Int): Boolean = {
    if (point >= 20) true else false
  }
}
