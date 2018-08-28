package Models

import scala.collection.mutable

case class GameCreatedResponse(wordName:String,category:String,alphabet:mutable.HashMap[String,Letter]) {

}
