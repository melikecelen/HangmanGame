package Models

import javax.inject.{Inject, Singleton}
import play.api.Configuration

@Singleton
case class Discount @Inject()(configuration: Configuration) extends Card {

  val name: String = configuration.underlying.getString("cards.Discount.name")
  val cost: Int = configuration.underlying.getInt("cards.Discount.cost")
  val availableCount: Int = configuration.underlying.getInt("cards.Discount.availableCount")

  override def isCardAffordable(point: Int): Boolean = {
    if (point >= 5 && point < 40) true else false
  }
}
