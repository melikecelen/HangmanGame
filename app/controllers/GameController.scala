package controllers

import javax.inject._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._
import javax.inject.{Inject, Singleton}
import play.api.libs.functional.syntax.unlift
import play.api.mvc.{AbstractController, ControllerComponents}
import services.GameService
import play.api.libs.json._
import play.api.libs.json.Reads._
import sun.util.logging.PlatformLogger

@Singleton
class GameController @Inject()(cc: ControllerComponents, gameService: GameService) extends AbstractController(cc) {



 /* implicit val levelWriters: Writes[Level] = (__ \ "level").write[Int] (unlift(Level.unapply))
  implicit val levelReads: Reads[Level] = (__ \ "level").read[Int] (Level.apply _)*/

  def startANewGame = Action(parse.json) { request =>
   /* val getLevel: JsValue = request.body
    val level = (getLevel \ "level").get.as[Int]
    if (gameService.currentGame.isEmpty) {
      gameService.createANewGame(level)
      Ok(Json.obj("status" -> "OK", "message" -> ("Game created")))
    }
    else BadRequest(Json.obj("status" -> "KO", "message" -> ("Invalid level")))*/
    /*level.validate[Int].fold(
      errors => {
        BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toJson(errors)))
      },
      level => {
        if(gameService.currentGame.isEmpty){
          gameService.createANewGame(level)
          Ok(Json.obj("status" -> "OK", "message" -> ("Game created")))
        }
        else BadRequest(Json.obj("status" -> "KO", "message" -> ("Invalid level")))
      }
    )*/

    val getLevel = request.body.validate[Int]

    getLevel.fold(
      errors => {
        BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toJson(errors)))
      },
      level => {
        if(gameService.currentGame.isEmpty){
          gameService.createANewGame(level)
          Ok(Json.obj("status" -> "OK", "message" -> ("Game created")))
        }
        else BadRequest(Json.obj("status" -> "KO", "message" -> ("Invalid level")))
      }
    )

  }

  def deneme = Action {
    Ok("dfdjkdg")
  }
}

case class Level(level: Int)

