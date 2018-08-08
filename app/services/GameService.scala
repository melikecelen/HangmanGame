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

  //TODO: If you do not return anything from here how are you going to pass game state to the UI?
  def makeANewGuess(letter: Option[String], cardName: Option[String], position: Option[Int]){
    if (currentGame.isDefined) {
      if (currentGame.get.isGameFinished()) {
        currentGame = None
      }
      else {
       // try {
          currentGame.get.makeANewGuess(findLetter(letter), findCard(cardName), position)
          None
        //}
       /* catch {
          case ex: ActiveCardException => println("There is a active card")
            Some( "There is a active card")
          case ex: UsedLetterException => println("This letter already choosed")
            Some( "This letter already choosed")
          case ex: OpenedPositionException => println("Position already opened")
            Some( "Position already opened")
          case ex: CardIsNotAvailableOrAffordableException =>println("Card isn't available or affordable! Make a new guess")
            Some("Card isn't available or affordable! Make a new guess")
          case ex: InvalidMoveException =>println("Invalid move")
            "Invalid move"
        }*/
      }
    }
    else throw new Exception("No game no cry")
  }

  def findCard(cardName:Option[String]): Option[Card]={
    if(cardName.isDefined){
      val card:Option[Card] = Some(cardService.getCards()(cardName.get))
      card
    }
    else None
  }

  def findLetter(letter: Option[String]):Option[Letter]={
    if(letter.isDefined){
      val myLetter:Option[Letter]=Some(alphabetService.getAlphabet()(letter.get.head))
      myLetter
    }
    else None
  }


}
