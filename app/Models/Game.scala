package Models

import scala.collection.{immutable, mutable}
import scala.collection.mutable.ListBuffer

class Game(level: Int, word: Word, cardHashMap: immutable.HashMap[String, Card], alphabet: mutable.HashMap[Char, Letter],
           point: Int, movesDefault: ListBuffer[MoveResult] = new mutable.ListBuffer[MoveResult], usedCardsDefault: ListBuffer[Card] = new mutable.ListBuffer[Card]) {

  var Point: Int = point
  val moves = movesDefault
  val usedCards = usedCardsDefault
  private var categoryName: Option[String] = None
  val gameWord: Word = word
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
        else return createAMove(letter, Some(card.get.name), None)
      } else throw new CardIsNotAvailableOrAffordableException
    }
    letter.flatMap { _ =>
      if (!moves.exists(m => m.letter == letter)) {
        if (checkLastMove()) {

          moves.last.card.get match {
            case "Risk" => return createAMove(letter, None, None)
            case "Consolation" => return createAMove(letter, None, Some(letter.get.cost / 2))
          }
        }

      } else throw new UsedLetterException()
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

  def createAMove(letter: Option[Letter], cardName: Option[String], cost: Option[Int]): Option[MoveResult] = {

    if (letter.isEmpty) {
      val gameState = isGameFinished()
      moves += MoveResult(None, cardName, None, word.getSecretWord(), Point, categoryName, gameState)
    }
    else if (!word.isLetterExist(letter.get)) {

      if (cost.isDefined)
        Point = reducePoint(cost.get)
      val gameState = isGameFinished()
      moves += MoveResult(letter, cardName, Some(false), word.getSecretWord(), Point, categoryName, gameState)
    }
    else {
      val gameState = isGameFinished()
      moves += MoveResult(letter, cardName, Some(true), word.getSecretWord(), Point, categoryName, gameState)
    }
    println(Point)
    Some(moves.last)
  }

  def checkLastMove(): Boolean = moves.nonEmpty && moves.last.card.isDefined &&
    ((moves.last.card.get == "Risk" && moves.last.result.get) ||
      (moves.last.card.get == "Consolation" && !moves.last.result.get))
}
