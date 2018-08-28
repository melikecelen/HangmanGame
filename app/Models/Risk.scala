package Models

import javax.inject.{Inject, Singleton}
import play.api.Configuration

@Singleton
class Risk @Inject()(configuration: Configuration) extends Card(configuration.underlying.getString("cards.Risk.name"),configuration.underlying.getInt("cards.Risk.cost"),configuration.underlying.getInt("cards.Risk.availableCount"),1) {

 /* val name: String = configuration.underlying.getString("cards.Risk.name")
  val cost: Int = configuration.underlying.getInt("cards.Risk.cost")
  val availableCount: Int = configuration.underlying.getInt("cards.Risk.availableCount")
  override val letterCostMultiplier: Double = 1
*/
  override def isCardAffordable(point: Int): Boolean = {
    if (point > 25 && point < 50) true else false
  }
}
