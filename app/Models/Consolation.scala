package Models

import javax.inject.{Inject, Singleton}
import play.api.Configuration

@Singleton
class Consolation @Inject()(configuration: Configuration) extends Card(configuration.underlying.getString("cards.Consolation.name"),configuration.underlying.getInt("cards.Consolation.cost"),configuration.underlying.getInt("cards.Consolation.availableCount"), 1,5,100) {

/*  val name: String = configuration.underlying.getString("cards.Consolation.name")
  val cost: Int = configuration.underlying.getInt("cards.Consolation.cost")
  val availableCount: Int = configuration.underlying.getInt("cards.Consolation.availableCount")
  override val letterCostMultiplier: Double = 1
*/
  override def isCardAffordable(point: Int): Boolean = {
    if (point >= 5) true else false
  }

}
