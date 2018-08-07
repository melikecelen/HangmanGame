package services

import Models.{Card, Game, Letter}
import javax.inject.{Inject, Singleton}
import play.api.Configuration


@Singleton
class GameService @Inject()(wordService: WordService, cardService: CardService, configuration: Configuration) {

  var currentGame: Option[Game] = None

  def createANewGame(level: Int) = {
    currentGame = Some(new Game(level, wordService.getRandomWord(level), cardService.getCards()))
  }

  def makeANewGuess(letter: Option[Letter], card: Option[Card], position: Option[Int]): Unit = {
    if (currentGame.isDefined) {
      if (currentGame.get.isGameFinished()) {
        currentGame = None
      }
      else {
        currentGame.get.makeANewGuess(letter, card,position)
      }
    }
  }


}
