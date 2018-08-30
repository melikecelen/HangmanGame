package Models


import scala.collection.mutable.ListBuffer


case class Card(name: String,cost: Int,availableCount: Int,letterCostMultiplier: Double = 0,minPoint:Int,maxPoint:Int){
 /* val name: String
  val cost: Int
  val availableCount: Int
  val letterCostMultiplier: Double = 0*/

  def isCardAffordable(p: Int): Boolean={false}

  def isCardAvailable(usedCards:ListBuffer[Card]):Boolean={
    if(usedCards.count( c => c.name == this.name)<this.availableCount)
      true
    else false
  }
}
