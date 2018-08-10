package Models

import scala.collection.mutable.ListBuffer

class Word(wordName:String, categoryName:String) {
  val name=wordName
  val category=categoryName

  var visibility:Array[Boolean]= new Array[Boolean](wordName.length)

  findSpace()

  def isLetterExist(l:Letter):Boolean={
    val list=findLetterPosition(l)
    if(list.isEmpty){
      false
    }
    else{
      setPositionsStatus(list)
      true
    }
  }

  def findLetterPosition(l: Letter): List[Int] = {

    var positions = new ListBuffer[Int]()
    var a: Int = -1
    /* if(wordName.indexOf(0)==l.letter)
       positions+=a+1*/
    for (c <- wordName if c == l.letter.head) {
      a = wordName.indexOf(c, a+1)
      positions += a
    }
    positions.toList
  }

  def setPositionsStatus(list: List[Int]): Unit = {
    list.foreach(l => visibility(l) = true)
  }

  def isAllPositionsRevealed(): Boolean = visibility.exists(_ != true)


  def showWord(){
    for (i <- 0 until  wordName.length) {
      if (visibility(i) == false)
        print("_ ")
      else print(wordName(i))
    }
  }

  def getSecretWord():String ={
    var secretword:Array[Char]=new Array[Char](wordName.length)
    for(i<-0 until wordName.length){
      if(visibility(i)==false){
        secretword(i)='*'
      }
      else secretword(i)=wordName(i)
    }
    secretword.mkString
  }

  def findSpace(): Unit ={
    for(c <- wordName if c.isWhitespace) {
      visibility(wordName.indexOf(c))=true
    }
  }
}