package Models

import scala.collection.{mutable, immutable}
import scala.collection.mutable.ListBuffer

class Game(level: Int, word: Word, cardHashMap: immutable.HashMap[String, Card], alphabet: mutable.HashMap[Char, Letter], point: Int) {

  var Point: Int = point
  val alphabet1: mutable.HashMap[Char, Letter] = alphabet
  // val cards:List[Card] = cardList
  val moves = ListBuffer[Move]()
  val usedCards = ListBuffer[Card]()
  var categoryName: Option[String] = None
  // var secretWord:String
  //  println("secret"+secretWord)
  println(word.name + "->" + word.category)

  def makeANewGuess(letter: Option[Letter], card: Option[Card], position: Option[Int]): Option[Move] = {
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
                    createAMove(letter, Some(card.get.name), letter.get.cost * 25 / 100)
                  case risk: Risk =>
                    createAMove(letter, Some(card.get.name), letter.get.cost)
                  case consolation: Consolation =>
                    createAMove(letter, Some(card.get.name), letter.get.cost)
                  case _ =>throw new InvalidMoveException()
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
                  if(position.isEmpty)
                    throw new InvalidMoveException()
                  else if (!word.visibility(position.get)) {
                    Point = reducePoint(card.get.cost)
                    word.visibility(position.get) = true
                    usedCards += card.get
                  }
                  else throw new OpenedPositionException()

                case category: Category => println(word.category)
                  Point = reducePoint(card.get.cost)
                  usedCards += card.get
                  categoryName = Some(word.category)
                  createAMove(None, Some(card.get.name), 0)
                case _ =>throw new InvalidMoveException()

              }
          }
        }
        else throw new CardIsNotAvailableOrAffordableException()
      }

      else if (letter.isDefined && position.isEmpty) {
        if (!moves.exists(m => m.letter == letter)) {
          if (checkLastMove()) {
            moves.last.card.get match {
              case "Risk" => createAMove(letter, None, 0)
              case "Consolation" => createAMove(letter, None, letter.get.cost / 2)
              case _ =>throw new InvalidMoveException()

            }
          }
          else createAMove(letter, None, letter.get.cost)
        }
        else throw new UsedLetterException()
      }
      else throw new InvalidMoveException()

      //word.showWord()
      println(word.getSecretWord())
      println("Point:" + Point)

    }
    if (isGameFinished()) {
      throw new GameFinishedException
    }

    Some(moves.last)

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

  def createAMove(letter: Option[Letter], card: Option[String], cost: Int): Option[Move] = {
    if (letter.isEmpty) {
      moves += Move(None, card, None, word.getSecretWord(), Point, categoryName)
    }
    else if (!word.isLetterExist(letter.get)) {
      Point = reducePoint(cost)
      moves += Move(letter, card, Some(false), word.getSecretWord(), Point, categoryName)
      //Some(Move(letter, card, Some(false),word.getSecretWord))
    }
    else moves += Move(letter, card, Some(true), word.getSecretWord(), Point, categoryName)
    //Some(Move(letter, card, Some(true),word.getSecretWord))
    Some(moves.last)
  }

  def checkLastMove(): Boolean = moves.nonEmpty && moves.last.card.isDefined &&
    ((moves.last.card.get == "Risk" && moves.last.result.get) ||
      (moves.last.card.get == "Consolation" && !moves.last.result.get))

}
