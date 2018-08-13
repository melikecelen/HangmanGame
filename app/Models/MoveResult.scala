package Models

case class MoveResult(letter: Option[Letter], card: Option[String], result: Option[Boolean],secretWord:String,point:Int,catName:Option[String],gameState: GameState)
