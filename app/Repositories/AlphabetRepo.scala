package Repositories


import com.typesafe.config.ConfigObject
import javax.inject.Inject
import play.api.Configuration

import scala.collection.mutable


class AlphabetRepo @Inject()(configuration: Configuration){
  val alphabet: java.util.List[_ <: ConfigObject] = configuration.underlying.getObjectList("alphabet")
  var alphabetMap: mutable.HashMap[Char, Int] = mutable.HashMap.empty

  alphabet.forEach(confObj => {
    val letter = confObj.toConfig.getString("letter").charAt(0) //atKey("letter")
    val cost = confObj.toConfig.getInt("cost") //atKey("cost")
    alphabetMap(letter)=cost
    //letter -> cost
  })

 /* val alphabet1: java.util.List[_ <: ConfigObject] = configuration.underlying.getObjectList("alphabet1")
  alphabet1.forEach(confObj => {

  })*/


}
