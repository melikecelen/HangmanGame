package Models

import javax.inject.{Inject, Singleton}
import play.api.Configuration

@Singleton
class Category @Inject()(configuration: Configuration) extends Card {

  val name: String = configuration.underlying.getString("cards.Category.name")
  val cost: Int = configuration.underlying.getInt("cards.Category.cost")
  val availableCount: Int = configuration.underlying.getInt("cards.Category.availableCount")

  override def isCardAffordable(point: Int): Boolean = {
    if (point >= 5) true else false
  }
}
