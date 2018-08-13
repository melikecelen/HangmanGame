package Models


import scala.collection.mutable.ListBuffer


abstract class Card{
  val name: String
  val cost: Int
  val availableCount: Int
  val letterCostMultiplier: Double = 0

  def isCardAffordable(p: Int): Boolean

  def isCardAvailable(usedCards:ListBuffer[Card]):Boolean={
    if(usedCards.count( c => c.name == this.name)<this.availableCount)
      true
    else false
  }
}
