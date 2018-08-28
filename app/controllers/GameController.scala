package controllers

import java.io.FileNotFoundException

import Models._
import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, ControllerComponents}
import services.GameService
import play.api.libs.json._
import scala.collection.mutable.Map

@Singleton
class GameController @Inject()(cc: ControllerComponents, gameService: GameService) extends AbstractController(cc) with JsonFormatters {
  def startANewGame = Action(parse.json) { request =>
    val getLevel = request.body.validate[Level]
    getLevel.fold(
      errors => {
        BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toJson(errors)))
      },
      level => {
        try {
          gameService.createANewGame(level.level)
          val alphabet = gameService.alphabet
          val word = gameService.currentGame.get.gameWord.getSecretWord()
          val point = gameService.currentGame.get.Point
          val cardList = gameService.cardList
          val moveList = gameService.currentGame.get.moves
         // val createResponseData:GameCreatedResponse=GameCreatedResponse(word.name,word.category,alphabet.values)
          //Ok(json.toJson(data.toMap))
         // Json.toJson(alphabet)
          Ok(Json.obj("status" -> "OK", /*"message" -> Json.toJson(word,alphabet.values.toArray),*/"point"->Json.toJson(point), "secretWord"->Json.toJson(word),
            "alphabet"->Json.toJson(alphabet.values.toArray),"cardList"->Json.toJson(cardList.values,"moveList"->Json.toJson(moveList))/*Json.toJson(cardList.keys.toArray)*/))
         // Ok(Json.obj("status" -> "OK", "message" -> Json.toJson(createResponseData)))

        }
        catch {
          case ex: FileNotFoundException => Ok(Json.obj("status" -> "KO", "message" -> "Word files not found"))
          case ex: InvalidLevelException => BadRequest(Json.obj("status"->"KO","message"->"Invalid level"))
        }
      }
    )
  }

  def makeANewGuess = Action(parse.json) { request =>
    val newGuess = request.body.validate[Guess]
    newGuess.fold(
      errors => {
        BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toJson(errors)))
      },
      guess => {
        try {
          val newMove: Option[MoveResult] = gameService.makeANewGuess(guess.letter, guess.cardName, guess.position)
          val moveList = gameService.currentGame.get.moves
          Ok(Json.obj("status" -> "OK", "message" -> Json.toJson(newMove.get), "categoryName"->Json.toJson(newMove.get.catName),"moves"->Json.toJson(moveList)))
        }
        catch {
          case ex: ActiveCardException => println("There is a active card")
            Ok(Json.obj("status" -> "KO", "message" -> "There is a active card"))
          case ex: UsedLetterException => println("This letter already choosed")
            Ok(Json.obj("status" -> "KO", "message" -> "This letter already choosed"))
          case ex: OpenedPositionException => println("Position already opened")
            Ok(Json.obj("status" -> "KO", "message" -> "Position already opened"))
          case ex: CardIsNotAvailableOrAffordableException => println("Card isn't available or affordable! Make a new guess")
            Ok(Json.obj("status" -> "KO", "message" -> "Card isn't available or affordable! Make a new guess"))
          case ex: InvalidMoveException => println("Invalid move")
            Ok(Json.obj("status" -> "KO", "message" -> "Invalid move"))
          case ex: GameFinishedException => println("Game finished")
            Ok(Json.obj("status" -> "KO", "message" -> "There is no game, please start a new game!"))
        }
      }
    )
  }
}

case class Level(level: Int)

case class Guess(letter: Option[String], cardName: Option[String], position: Option[Int])

