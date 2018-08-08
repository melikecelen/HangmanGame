package controllers

import Models.{Card, Letter, Move}
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

  implicit val levelReads: Reads[Level] =
    (JsPath \ "level").read[Int].map(Level.apply)

  /*implicit val levelWrites: Writes[Level] = (
    (JsPath \ "level").write[Int] and
      (JsPath \ "level1").write[Int]
    ) (unlift(Level.unapply))*/

 /* implicit val cardWriters: Writes[Card] = (
    (JsPath \ "name").write[String] and
      (JsPath \ "cost").write[Int] and
      (JsPath \ "availableCount").write[Int]
    ) (unlift(Card.unapply))*/

  implicit val letterWriters: Writes[Letter] = (
    (__ \ "l").write[String] and
      (__ \ "c").write[Int]
    ) (unlift(Letter.unapply))


  implicit val moveWriters: Writes[Move] = (
    (JsPath \ "letter").writeNullable[Letter] and
      (JsPath \ "cardName").writeNullable[String] and
      (JsPath \ "result").writeNullable[Boolean]
    ) (unlift(Models.Move.unapply))

  /* implicit val letterWriter = Json.writes[Letter]
   implicit val cardWriter = Json.writes[Card]
   implicit val moveWriter = Json.writes[Move]*/


  /*implicit val cardReads: Reads[Card] = (
    (JsPath \ "name").read[String] and
      (JsPath \ "cost").read[Int] and
      (JsPath \ "availableCount").read[Int]
    ) (Card.apply _)*/

  implicit val letterReads: Reads[Letter] = (
    (JsPath \ "l").read[String] and
      (JsPath \ "c").read[Int]
    ) (Letter.apply _)

  implicit val moveReads: Reads[Move] = (
    (JsPath \ "letter").readNullable[Letter] and
      (JsPath \ "cardName").readNullable[String] and
      (JsPath \ "result").readNullable[Boolean]
    ) (Models.Move.apply _)

  implicit val guessReads: Reads[Guess] = (
    (JsPath \ "letter").readNullable[String] and
      (JsPath \ "card").readNullable[String] and
      (JsPath \ "position").readNullable[Int]
    ) (Guess.apply _)


  def startANewGame = Action(parse.json) { request =>

    val getLevel = request.body.validate[Level]

    getLevel.fold(
      errors => {
        BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toJson(errors)))
      },
      level => {
        if (gameService.currentGame.isEmpty) {
          gameService.createANewGame(level.level)
          Ok(Json.obj("status" -> "OK", "message" -> ("Game created")))
        }
        else BadRequest(Json.obj("status" -> "KO", "message" -> ("Invalid level")))
      }
    )

  }

  def getAlphabet=Action{
    Ok("hello")
  }

  def makeANewGuess = Action(parse.json) { request =>
    val newGuess = request.body.validate[Guess]
    newGuess.fold(
      errors => {
        BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toJson(errors)))
      },
      guess => {
        gameService.currentGame.get.makeANewGuess(Some(gameService.currentGame.get.alphabet1(guess.letter.get.head)),None,guess.position)
        Ok(gameService.currentGame.get.Point.toString)

      }
    )
  }

  def listMoves = Action {
    val json = Json.toJson(Move.list)
    Ok(json)
  }

  def saveMove = Action(parse.json) { request =>
    val moveResult = request.body.validate[Move]
    moveResult.fold(
      errors => {
        BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toJson(errors)))
      },
      move => {
        Move.addMove(move)
       // gameService.currentGame.get.makeANewGuess(move.letter,gameService.currentGame.get.cards.find(c => c.name==move.cardName),None)
        Ok(Json.obj("status" -> "OK", "message" -> ("Move saved"))+gameService.currentGame.get.Point.toString)
      }
    )

  }

  def deneme = Action {
    gameService.createANewGame(1)
    Ok(gameService.currentGame.get.Point.toString)
  }
}

case class Level(level: Int) {}

case class Guess(letter: Option[String], cardName:Option[String], position: Option[Int])

object Move{
  var list:List[Move] ={
    List(

    )
  }

  def addMove(move:Move): Unit ={
    list = list ::: List(move)
  }
}