package Repositories

import Models._
import javax.inject.Inject
import play.api.Configuration


class CardRepo @Inject()(configuration: Configuration, category: Category,
                         consolation: Consolation, buyALetter: BuyALetter,
                         discount: Discount, risk: Risk) {

  def getCardList() :List[Card] = {
    val cardList = List[Card](category,consolation,buyALetter,discount,risk)
    cardList
  }

}
