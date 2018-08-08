package Repositories

import Models._
import javax.inject.Inject
import play.api.Configuration

import scala.collection.{immutable, mutable}


class CardRepo @Inject()(configuration: Configuration, category: Category,
                         consolation: Consolation, buyALetter: BuyALetter,
                         discount: Discount, risk: Risk) {

  def getCardList(): immutable.HashMap[String, Card] = {
    val cardList = List[Card](category, consolation, buyALetter, discount, risk)
    val cardHashMap = immutable.HashMap[String, Card]("Category" -> category, "Consolation" -> consolation, "Buy A Letter" -> buyALetter, "Discount" -> discount, "Risk" -> risk)
    cardHashMap
  }

  //configuration.underlying.getObject("cards.Category")

}
