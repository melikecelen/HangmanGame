package Repositories


import Models.Letter
import com.typesafe.config.ConfigObject
import javax.inject.Inject
import play.api.Configuration

import scala.collection.mutable


class AlphabetRepo @Inject()(configuration: Configuration){
  val alphabet: java.util.List[_ <: ConfigObject] = configuration.underlying.getObjectList("alphabet")
  var alphabetMap: mutable.HashMap[Char, Letter] = mutable.HashMap.empty

  alphabet.forEach(confObj => {
    val letter = confObj.toConfig.getString("letter") //atKey("letter")
    val cost = confObj.toConfig.getInt("cost") //atKey("cost")
   // alphabetMap(letter)=cost
    alphabetMap += letter.head -> new Letter(letter,cost)
    println(alphabetMap(letter.head).letter)
    //letter -> cost
  })

 /* val alphabet1: java.util.List[_ <: ConfigObject] = configuration.underlying.getObjectList("alphabet1")
  alphabet1.forEach(confObj => {

  })*/


}
