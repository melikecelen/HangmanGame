package services

import Models.Word
import Repositories.WordRepo
import javax.inject.{Inject, Singleton}

@Singleton
class WordService @Inject()(wR: WordRepo) {

  def getRandomWord(level: Int):Word = {
    wR.getRandomWord(level)
  }

}
