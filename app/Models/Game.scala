package Models

import scala.collection.{immutable, mutable}
import scala.collection.mutable.ListBuffer

class Game(level: Int, word: Word, cardHashMap: immutable.HashMap[String, Card], alphabet: mutable.HashMap[Char, Letter], point: Int) {

  var Point: Int = point
   val moves = ListBuffer[MoveResult]()
   val usedCards = ListBuffer[Card]()
  private var categoryName: Option[String] = None

  println(word.name + "->" + word.category)

  def makeANewGuess(letter: Option[Letter], card: Option[Card], position: Option[Int]): Option[MoveResult] = {
    card.flatMap { c =>
      if (c.isCardAvailable(usedCards) && c.isCardAffordable(Point)) {
        if (checkLastMove()) throw new ActiveCardException
        c match {
          case buyALetter: BuyALetter => buyALetter.useBuyALetter(word, position.get)
          case category: Category => categoryName = Some(word.category)
          case _ =>
        }
        Point = reducePoint(card.get.cost)
        usedCards += card.get
        if (letter.isDefined)
          return createAMove(letter, Some(card.get.name), Some((card.get.letterCostMultiplier * letter.get.cost).toInt))
        else return return createAMove(letter, Some(card.get.name), None)
      } else throw new CardIsNotAvailableOrAffordableException
    }
    letter.flatMap { _ =>
      if (checkLastMove()) {
        if (!moves.exists(m => m.letter == letter)) {
          moves.last.card.get match {
            case "Risk" => return createAMove(letter, None, None)
            case "Consolation" => return createAMove(letter, None, Some(letter.get.cost / 2))
          }
        }
        else throw new UsedLetterException()
      }
      return createAMove(letter, None, Some(letter.get.cost))
    }
  }

  def reducePoint(total: Int): Int = {
    Point - total
  }

  def isGameFinished(): GameState = {
    if (!word.isAllPositionsRevealed()) {
      println("You won!")
      GameState(finished = true, "You won")
    }
    else if (Point < 0) {
      println("You lost")
      GameState(finished = true, "You lost")
    }
    else GameState(finished = false, "Continue")
  }

  def isMoveValid(letter: Option[Letter], card: Option[Card], position: Option[Int]): Boolean = {
    if (letter.isDefined && card.isDefined && position.isEmpty) {
      if (card.get.name == "Consolation" || card.get.name == "Discount" || card.get.name == "Risk")
        return true
    }
    else if (letter.isEmpty && card.isDefined && position.isDefined) {
      if (card.get.name == "Buy A Letter") return true
    }
    else if (letter.isEmpty && card.isDefined && position.isEmpty) {
      if (card.get.name == "Category") return true
    }
    else if (letter.isDefined && card.isEmpty && position.isEmpty)
      return true
    false
  }

  def createAMove(letter: Option[Letter], cardName: Option[String], cost: Option[Int]): Option[MoveResult] = {
    val gameState = isGameFinished()
    if (letter.isEmpty) {
      moves += MoveResult(None, cardName, None, word.getSecretWord(), Point, categoryName, gameState)
    }
    else if (!word.isLetterExist(letter.get)) {
      Point = reducePoint(cost.get)
      moves += MoveResult(letter, cardName, Some(false), word.getSecretWord(), Point, categoryName, gameState)
      //Some(Move(letter, card, Some(false),word.getSecretWord))
    }
    else moves += MoveResult(letter, cardName, Some(true), word.getSecretWord(), Point, categoryName, gameState)
    println(Point)
    Some(moves.last)
  }

  def checkLastMove(): Boolean = moves.nonEmpty && moves.last.card.isDefined &&
    ((moves.last.card.get == "Risk" && moves.last.result.get) ||
      (moves.last.card.get == "Consolation" && !moves.last.result.get))
}
