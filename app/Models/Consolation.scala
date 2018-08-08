package Models

import javax.inject.{Inject, Singleton}
import play.api.Configuration

@Singleton
class Consolation @Inject()(configuration: Configuration) extends Card {

  val name: String = configuration.underlying.getString("cards.Consolation.name")
  val cost: Int = configuration.underlying.getInt("cards.Consolation.cost")
  val availableCount: Int = configuration.underlying.getInt("cards.Consolation.availableCount")

  println(name + cost + availableCount)
  override def isCardAffordable(point: Int): Boolean = {
    if (point >= 5) true else false
  }

}
