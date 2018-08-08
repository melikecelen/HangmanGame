package Models

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class Game(level: Int, word: Word, cardList: List[Card],alphabet: mutable.HashMap[Char,Letter]) {

  var Point: Int = 100
  val alphabet1:mutable.HashMap[Char,Letter] = alphabet
  val cards:List[Card] = cardList
  val moves = ListBuffer[Move]()
  val usedCards = ListBuffer[Card]()

  // println(word.name + "->" + word.category)

  def makeANewGuess(letter: Option[Letter], card: Option[Card], position: Option[Int]): Unit = {
   // try {
      if (!isGameFinished()) {
        if (card.isDefined) {
          if (card.get.isCardAvailable(usedCards) && card.get.isCardAffordable(Point)) {
            if (letter.isDefined && position.isEmpty) {
              if (!moves.exists(m => m.letter == letter)) {
                if (checkLastMove())
                  throw new ActiveCardException()
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
              else throw new UsedLetterException()
            }
            else {
              if (checkLastMove())
                throw new ActiveCardException()
              else
                card.get match {
                  case buyALetter: BuyALetter =>
                    if (!word.visibility(position.get)) {
                      Point = reducePoint(card.get.cost)
                      word.visibility(position.get) = true
                      usedCards += card.get
                    }
                    else throw new OpenedPositionException()

                  case category: Category => println(word.category)
                    Point = reducePoint(card.get.cost)
                    usedCards += card.get
                }
            }
          }
          else throw new CardIsNotAvailableOrAffordableException()
        }
        else if (letter.isDefined && position.isEmpty) {
          if (!moves.exists(m => m.letter == letter)) {
            if (checkLastMove()) {
              moves.last.cardName.get match {
                case "Risk" => createAMove(letter, card, 0)
                case "Consolation" => createAMove(letter, card, letter.get.cost / 2)
              }
            }
            else createAMove(letter, card, letter.get.cost)
          }
          else throw new UsedLetterException()
        }
        else throw new InvalidMoveException()

        word.showWord()
        println("Point:" + Point)
      }
   // }
  /*  catch {
      case ex: ActiveCardException => println("There is a active card")
      case ex: UsedLetterException => println("This letter already choosed")
      case ex: OpenedPositionException => println("Position already opened")
      case ex: CardIsNotAvailableOrAffordableException =>println("Card isn't available or affordable! Make a new guess")
      case ex: InvalidMoveException =>println("Invalid move")

    }*/
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
      moves += Move(letter, /*Some(card.get.name)*/None, Some(false))
      Point = reducePoint(cost)
    }
    else moves += Move(letter, /*Some(card.get.name)*/None, Some(true))
  }

  def checkLastMove(): Boolean = {
    moves.nonEmpty && moves.last.cardName.isDefined && ((moves.last.cardName.get == "Risk" && moves.last.result.get) ||
      (moves.last.cardName.get == "Consolation" && !moves.last.result.get))
  }

}
