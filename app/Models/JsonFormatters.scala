package Models

import controllers.{Guess, Level}
import javax.inject.Singleton
import play.api.libs.json.{JsPath, Reads, Writes, __}
import play.api.libs.functional.syntax._
import play.api.libs.functional.syntax.unlift
import play.api.libs.json.Reads._

import scala.collection.mutable

@Singleton
trait JsonFormatters {
  implicit val levelReads: Reads[Level] =
    (JsPath \ "level").read[Int].map(Level.apply)


  implicit val letterWriters: Writes[Letter] = (
    (__ \ "letter").write[String] and
      (__ \ "cost").write[Int]
    ) (unlift(Letter.unapply))
  implicit val letterReads: Reads[Letter] = (
    (JsPath \ "letter").read[String] and
      (JsPath \ "cost").read[Int]
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

 // Card(name: String,cost: Int,availableCount: Int,letterCostMultiplier: Double = 0)
  implicit val cardWriters: Writes[Card]=(
    (JsPath \ "name").write[String] and
      (JsPath \ "cost").write[Int] and
      (JsPath \ "availableCount").write[Int] and
      (JsPath \ "letterCostMultiplier").write[Double]
    )(unlift(Models.Card.unapply))
  implicit val cardReads: Reads[Card]=(
    (JsPath \ "name").read[String] and
      (JsPath \ "cost").read[Int] and
      (JsPath \ "availableCount").read[Int] and
      (JsPath \ "letterCostMultiplier").read[Double]
    )(Models.Card.apply _)

  implicit val createResposeWriters:Writes[GameCreatedResponse]=(
    (JsPath \ "wordName").write[String] and
      (JsPath \ "category").write[String] and
      (JsPath \ "alphabet").write[mutable.HashMap[String,Letter]]
  )(unlift(GameCreatedResponse.unapply))







}
