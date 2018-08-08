package Repositories

import java.io.{File, FileNotFoundException}

import Models.Word
import javax.inject.{Inject, Singleton}
import play.api.Configuration

import scala.collection.mutable.ListBuffer
import scala.io.Source

@Singleton
class WordRepo @Inject()(configuration: Configuration){
 /* val allWords = Map[Int, ListBuffer[Word]](1 -> ListBuffer[Word](), 2 -> ListBuffer[Word](), 3 -> ListBuffer[Word]())

  def getRandomWord(level: Int): Word = {
    val random = scala.util.Random.nextInt(allWords(level).length)
    allWords(level)(random)
  }*/

  val allWords = Map[Int, ListBuffer[Word]](1 -> ListBuffer[Word](), 2 -> ListBuffer[Word](),
    3 -> ListBuffer[Word]())
def findFiles(): Unit ={
  val filesHere = new File(".").listFiles
  val files = filesHere.filter(file => file.getName.endsWith(".txt"))
  if(files.isEmpty){
    throw new FileNotFoundException()
  }
  files.foreach(file =>
    Source.fromFile(file.getName).getLines.foreach(line =>
      if (line.length >= 10) allWords(1) += new Word(line.toLowerCase, file.getName.dropRight(4))
      else if (line.length >= 6) allWords(2) += new Word(line.toLowerCase, file.getName.dropRight(4))
      else allWords(3) += new Word(line.toLowerCase, file.getName.dropRight(4))))
}


  //allWords(1).foreach(word => println(word.category + "----" + word.name))


  def getRandomWord(level: Int): Word = {
      findFiles()
      val random = scala.util.Random.nextInt(allWords(level).length)
      allWords(level)(random)

  }
}
