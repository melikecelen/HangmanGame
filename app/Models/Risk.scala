package Models

import javax.inject.{Inject, Singleton}
import play.api.Configuration

@Singleton
class Risk @Inject()(configuration: Configuration) extends Card {

  val name: String = configuration.underlying.getString("cards.Risk.name")
  val cost: Int = configuration.underlying.getInt("cards.Risk.cost")
  val availableCount: Int = configuration.underlying.getInt("cards.Risk.availableCount")

  override def isCardAffordable(point: Int): Boolean = {
    if (point > 25 && point < 50) true else false
  }
}
