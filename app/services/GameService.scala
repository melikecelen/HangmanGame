package services

import Models._
import javax.inject.{Inject, Singleton}
import play.api.Configuration

import scala.collection.mutable


@Singleton
class GameService @Inject()(wordService: WordService, cardService: CardService, alphabetService: AlphabetService, configuration: Configuration) {

  var currentGame: Option[Game] = None

  def createANewGame(level: Int, point:Int = configuration.underlying.getInt("point")) = {
    currentGame = Some(new Game(level, wordService.getRandomWord(level), cardService.getCards(), alphabetService.getAlphabet(),point))
  }

  def makeANewGuess(letter: Option[String], cardName: Option[String], position: Option[Int]): Option[Move] = {
    if (currentGame.isDefined) {
      if (currentGame.get.isGameFinished()) {
        currentGame = None
        None
      }
      else {
        val newMove: Option[Move] = currentGame.get.makeANewGuess(findLetter(letter), findCard(cardName), position)
        newMove
      }
    }
    else throw new Exception("No game no cry")
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


}
