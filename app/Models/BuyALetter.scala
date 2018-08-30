package Models

import javax.inject.{Inject, Singleton}
import play.api.Configuration

import scala.collection.mutable.ListBuffer
@Singleton
class BuyALetter @Inject()(configuration: Configuration) extends Card(configuration.underlying.getString("cards.BuyALetter.name"),configuration.underlying.getInt("cards.BuyALetter.cost"),configuration.underlying.getInt("cards.BuyALetter.availableCount"),0,20,100) {
 /* val name: String = configuration.underlying.getString("cards.BuyALetter.name")
  val cost: Int = configuration.underlying.getInt("cards.BuyALetter.cost")
  val availableCount: Int = configuration.underlying.getInt("cards.BuyALetter.availableCount")
*/
  override def isCardAffordable(point: Int): Boolean = {
    if (point >= 20) true else false
  }
def useBuyALetter(word: Word,position:Int) {
  if (!word.visibility(position)) {
    word.visibility(position) = true

  } else {
   // usedCards.drop(usedCards.length - 1)
    throw new OpenedPositionException()
  }
}

}
