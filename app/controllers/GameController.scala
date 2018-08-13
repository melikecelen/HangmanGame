package controllers

import Models._

import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, ControllerComponents}
import services.GameService
import play.api.libs.json._

@Singleton
class GameController @Inject()(cc: ControllerComponents, gameService: GameService) extends AbstractController(cc) with JsonFormatters {
  def startANewGame = Action(parse.json) { request =>
    val getLevel = request.body.validate[Level]
    getLevel.fold(
      errors => {
        BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toJson(errors)))
      },
      level => {
          gameService.createANewGame(level.level)
          Ok(Json.obj("status" -> "OK", "message" -> ("Game created")))
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
          val newMove:Option[MoveResult]=gameService.makeANewGuess(guess.letter, guess.cardName, guess.position)
          Ok(Json.obj("status" -> "OK", "message" -> Json.toJson(newMove.get)))
        }
        catch {
          case ex: ActiveCardException => println("There is a active card")
            Ok(Json.obj("status" -> "OK", "message" ->"There is a active card"))
          case ex: UsedLetterException => println("This letter already choosed")
            Ok(Json.obj("status" -> "OK", "message" ->"This letter already choosed"))
          case ex: OpenedPositionException => println("Position already opened")
            Ok(Json.obj("status" -> "OK", "message" ->"Position already opened"))
          case ex: CardIsNotAvailableOrAffordableException => println("Card isn't available or affordable! Make a new guess")
            Ok(Json.obj("status" -> "OK", "message" ->"Card isn't available or affordable! Make a new guess"))
          case ex: InvalidMoveException => println("Invalid move")
            Ok(Json.obj("status" -> "OK", "message" ->"Invalid move"))
          case ex: GameFinishedException => println("Game finished")
            Ok(Json.obj("status" -> "KO", "message" ->"There is no game, please start a new game!"))
        }
      }
    )
  }
}

case class Level(level: Int)

case class Guess(letter: Option[String], cardName: Option[String], position: Option[Int])

