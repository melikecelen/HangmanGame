def a(): Unit = {
  try {
  throw new Exception
}
catch {
  case exception: Exception => println("cücük")
}
}

def b(): Unit = {
    a()
}

b()

case class Letter(l:String,c:Int) {
  val letter=l
  val cost=c
}

case class Guess(letter: Option[Letter])
