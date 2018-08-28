package services

import Models._
import javax.inject.{Inject, Singleton}
import play.api.Configuration

import scala.collection.mutable
import scala.collection.mutable.ListBuffer


@Singleton
class GameService @Inject()(wordService: WordService, cardService: CardService, alphabetService: AlphabetService, configuration: Configuration) {

  var alphabet = alphabetService.getAlphabet()
  var cardList = cardService.getCards()
  var currentGame: Option[Game] = None

  def createANewGame(level: Int, point: Int = configuration.underlying.getInt("point"),
                     moves: ListBuffer[MoveResult] = new mutable.ListBuffer[MoveResult],
                     usedCards: ListBuffer[Card] = new mutable.ListBuffer[Card]) = {
    if (level <= 3 && level >= 1) {
      currentGame = Some(new Game(level, wordService.getRandomWord(level), cardService.getCards(), alphabetService.getAlphabet(), point))
    }
    else throw new InvalidLevelException
  }
  def createANewGameTest(level: Int, point: Int = configuration.underlying.getInt("point"),word: Word,
                     moves: ListBuffer[MoveResult] = new mutable.ListBuffer[MoveResult],
                     usedCards: ListBuffer[Card] = new mutable.ListBuffer[Card]) = {
    if (level <= 3 && level >= 1) {
      currentGame = Some(new Game(level,word, cardService.getCards(), alphabetService.getAlphabet(), point,moves,usedCards))
    }
  }
  def makeANewGuess(letter: Option[String], cardName: Option[String], position: Option[Int]): Option[MoveResult] = {
    if (!isMoveValid(letter, cardName, position)) throw new InvalidMoveException()
    if (currentGame.isDefined) {
      if (currentGame.get.isGameFinished().finished) {
        currentGame = None
        None
      }
      else {
        val newMove: Option[MoveResult] = currentGame.get.makeANewGuess(findLetter(letter), findCard(cardName), position)
        newMove
      }
    }
    else throw new GameFinishedException
  }

  def findCard(cardName: Option[String]): Option[Card] = {
    if (cardName.isDefined) {
      val card: Option[Card] = Some(cardService.getCards()(cardName.get))
      card
    }
    else None
  }

  def findLetter(letter: Option[String]): Option[Letter] = {
    if (letter.isDefined) {
      val myLetter: Option[Letter] = Some(alphabetService.getAlphabet()(letter.get.head))
      myLetter
    }
    else None
  }

  def isMoveValid(letter: Option[String], card: Option[String], position: Option[Int]): Boolean = {
    if (letter.isDefined && card.isDefined && position.isEmpty) {
      if (card.get == "Consolation" || card.get == "Discount" || card.get == "Risk")
        return true
    }
    else if (letter.isEmpty && card.isDefined && position.isDefined) {
      if (card.get == "Buy A Letter") return true
    }
    else if (letter.isEmpty && card.isDefined && position.isEmpty) {
      if (card.get == "Category") return true
    }
    else if (letter.isDefined && card.isEmpty && position.isEmpty)
      return true
    false
  }
}
