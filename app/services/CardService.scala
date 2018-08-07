package services

import Models.Card
import Repositories.CardRepo
import javax.inject.{Inject, Singleton}

@Singleton
class CardService @Inject()(cardRepo: CardRepo){
  def getCards(): List[Card] = {
    cardRepo.getCardList()
  }

}
