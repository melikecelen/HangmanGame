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