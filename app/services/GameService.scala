package services

import Models._
import javax.inject.{Inject, Singleton}
import play.api.Configuration

import scala.collection.mutable


@Singleton
class GameService @Inject()(wordService: WordService, cardService: CardService, alphabetService: AlphabetService, configuration: Configuration) {

  var currentGame: Option[Game] = None
  //val alphabet:mutable.HashMap[Char,Int] = alphabetService.getAlphabet()
  def createANewGame(level: Int) = {
    currentGame = Some(new Game(level, wordService.getRandomWord(level), cardService.getCards(),alphabetService.getAlphabet()))
  }

  def makeANewGuess(letter: Option[Letter], card: Option[Card], position: Option[Int]): Unit = {
    if (currentGame.isDefined) {
      if (currentGame.get.isGameFinished()) {
        currentGame = None
      }
      else {
        try {
          currentGame.get.makeANewGuess(letter, card, position)
        }
        catch {
          case ex: ActiveCardException => println("There is a active card")
          case ex: UsedLetterException => println("This letter already choosed")
          case ex: OpenedPositionException => println("Position already opened")
          case ex: CardIsNotAvailableOrAffordableException =>println("Card isn't available or affordable! Make a new guess")
          case ex: InvalidMoveException =>println("Invalid move")
        }
      }
    }
  }


}
