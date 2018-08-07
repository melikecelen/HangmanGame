package services

import Repositories.AlphabetRepo
import javax.inject.Inject

import scala.collection.mutable.HashMap


class AlphabetService @Inject()(alphabetRepo: AlphabetRepo) {
  def getAlphabet(): HashMap[Char,Int] = {
    alphabetRepo.alphabetMap
  }

}
