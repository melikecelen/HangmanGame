package Models

import controllers.{Guess, Level}
import javax.inject.Singleton
import play.api.libs.json.{JsPath, Reads, Writes, __}
import play.api.libs.functional.syntax._
import play.api.libs.functional.syntax.unlift
import play.api.libs.json.Reads._

@Singleton
trait JsonFormatters {
  implicit val levelReads: Reads[Level] =
    (JsPath \ "level").read[Int].map(Level.apply)


  implicit val letterWriters: Writes[Letter] = (
    (__ \ "l").write[String] and
      (__ \ "c").write[Int]
    ) (unlift(Letter.unapply))
  implicit val letterReads: Reads[Letter] = (
    (JsPath \ "l").read[String] and
      (JsPath \ "c").read[Int]
    ) (Letter.apply _)

  implicit val gameStateWriters: Writes[GameState] = (
    (__ \ "finished").write[Boolean] and
      (__ \ "message").write[String]
    ) (unlift(GameState.unapply))
  implicit val gameStateReads: Reads[GameState] = (
    (JsPath \ "finished").read[Boolean] and
      (JsPath \ "message").read[String]
    ) (GameState.apply _)

  implicit val guessReads: Reads[Guess] = (
    (JsPath \ "letter").readNullable[String] and
      (JsPath \ "cardName").readNullable[String] and
      (JsPath \ "position").readNullable[Int]
    ) (Guess.apply _)

  implicit val guessWrites: Writes[Guess] = (
    (JsPath \ "letter").writeNullable[String] and
      (JsPath \ "cardName").writeNullable[String] and
      (JsPath \ "position").writeNullable[Int]
    ) (unlift(Guess.unapply))

  implicit val moveWriters: Writes[MoveResult] = (
    (JsPath \ "letter").writeNullable[Letter] and
      (JsPath \ "card").writeNullable[String] and
      (JsPath \ "result").writeNullable[Boolean] and
      (JsPath \ "secretWord").write[String] and
      (JsPath \ "point").write[Int] and
      (JsPath \ "catName").writeNullable[String] and
      (JsPath \ "gameState").write[GameState]

    ) (unlift(Models.MoveResult.unapply))
  implicit val moveReads: Reads[MoveResult] = (
    (JsPath \ "letter").readNullable[Letter] and
      (JsPath \ "card").readNullable[String] and
      (JsPath \ "result").readNullable[Boolean] and
      (JsPath \ "secretWord").read[String] and
      (JsPath \ "point").read[Int] and
      (JsPath \ "catName").readNullable[String] and
      (JsPath \ "gameState").read[GameState]

    ) (Models.MoveResult.apply _)





}
