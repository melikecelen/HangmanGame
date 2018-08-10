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

  implicit val guessReads: Reads[Guess] = (
    (JsPath \ "letter").readNullable[String] and
      (JsPath \ "cardName").readNullable[String] and
      (JsPath \ "position").readNullable[Int]
    ) (Guess.apply _)

  implicit val guessWrites: Writes[Guess] = (
    (JsPath \ "letter").writeNullable[String] and
      (JsPath \ "cardName").writeNullable[String] and
      (JsPath \ "position").writeNullable[Int]
    )(unlift(Guess.unapply))

   implicit val moveWriters: Writes[Move] = (
   (JsPath \ "letter").writeNullable[Letter] and
     (JsPath \ "card").writeNullable[String] and
     (JsPath \ "result").writeNullable[Boolean] and
       (JsPath \ "secretWord").write[String] and
         (JsPath \ "point").write[Int]and
           (JsPath \ "catName").writeNullable[String]

     ) (unlift(Models.Move.unapply))
 implicit val moveReads: Reads[Move] = (
   (JsPath \ "letter").readNullable[Letter] and
     (JsPath \ "card").readNullable[String] and
     (JsPath \ "result").readNullable[Boolean] and
       (JsPath \ "secretWord").read[String] and
         (JsPath \ "point").read[Int]and
           (JsPath \ "catName").readNullable[String]

   ) (Models.Move.apply _)
}
