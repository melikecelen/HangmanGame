package Models

import scala.collection.mutable.ListBuffer

class Game (level: Int, word: Word, cardList: List[Card]) {

  var Point: Int = 100

  val moves = ListBuffer[Move]()
  val usedCards = ListBuffer[Card]()

 // println(word.name + "->" + word.category)

  def makeANewGuess(letter: Option[Letter], card: Option[Card], position: Option[Int]): Unit = {

    if (!isGameFinished()) {
      if (card.isDefined) {
        if (card.get.isCardAvailable(usedCards) && card.get.isCardAffordable(Point)) {
          if (letter.isDefined && position.isEmpty) {
            if (!moves.exists(m => m.letter == letter)) {
              if (checkLastMove())
                println("There is a active card")
              else {
                Point = reducePoint(card.get.cost)
                card.get match {
                  case discount: Discount =>
                    createAMove(letter, card, letter.get.cost * 25 / 100)
                  case risk: Risk =>
                    createAMove(letter, card, letter.get.cost)
                  case consolation: Consolation =>
                    createAMove(letter, card, letter.get.cost)
                }
                usedCards += card.get
              }
            }
            else println("This letter already choosed")
          }
          else {
            if (checkLastMove())
              println("There is a active card")
            else
              card.get match {
                case buyALetter:BuyALetter =>
                  if (!word.visibility(position.get)) {
                    Point = reducePoint(card.get.cost)
                    word.visibility(position.get) = true
                    usedCards += card.get
                  }
                  else println("Position already opened")

                case category: Category => println(word.category)
                  Point = reducePoint(card.get.cost)
                  usedCards += card.get
              }
          }
        }
        else println("Card isn't available or affordable! Make a new guess")
      }
      else if (letter.isDefined && position.isEmpty) {
        if (!moves.exists(m => m.letter == letter)) {
          if (checkLastMove()) {
            moves.last.card.get match {
              case risk: Risk => createAMove(letter, card, 0)
              case consolation: Consolation => createAMove(letter, card, letter.get.cost / 2)
            }
          }
          else createAMove(letter, card, letter.get.cost)
        }
        else println("This letter already choosed")
      }
      else println("Invalid move")

      word.showWord()
      println("Point:" + Point)


    }

  }

  def reducePoint(total: Int): Int = {
    Point - total
  }

  def isGameFinished(): Boolean = {
    if (!word.isAllPositionsRevealed()) {
      println("You won!")
      true
    }
    else if (Point < 0) {
      println("You lost")
      true
    }
    else
      false
  }

  def createAMove(letter: Option[Letter], card: Option[Card], cost: Int) {
    if (!word.isLetterExist(letter.get)) {
      moves += Move(letter, card, Some(false))
      Point = reducePoint(cost)
    }
    else moves += Move(letter, card, Some(true))
  }

  def checkLastMove(): Boolean = {
    moves.nonEmpty && moves.last.card.isDefined && ((moves.last.card.get == Risk && moves.last.result.get) ||
      (moves.last.card.get == Consolation && !moves.last.result.get))
  }

}
