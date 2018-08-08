package services

import Models.Card
import Repositories.CardRepo
import javax.inject.{Inject, Singleton}

import scala.collection.immutable

@Singleton
class CardService @Inject()(cardRepo: CardRepo){
  def getCards(): immutable.HashMap[String,Card] = {
    cardRepo.getCardList()
  }

}
