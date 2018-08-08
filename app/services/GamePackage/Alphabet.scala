package services.GamePackage

import Models.Letter

import scala.collection.immutable.Map

object Alphabet {
  val alphabet = Map[Char, Letter]('a' -> Letter("a", 18), 'b' -> Letter("b", 8),
    'c' -> Letter("c", 12))
}

//, 'd' -> Letter('d', 10), 'e' -> Letter('e', 20),
//    'f' -> Letter('f', 8), 'g' -> Letter('g', 9), 'h' -> Letter('h', 10),
//    'i' -> Letter('i', 16), 'j' -> Letter('j', 5), 'k' -> Letter('k', 6),
//    'l' -> Letter('l', 13), 'm' -> Letter('m', 10), 'n' -> Letter('n', 15),
//    'o' -> Letter('o', 15), 'q' -> Letter('q', 5), 'p' -> Letter('p', 10),
//    'r' -> Letter('r', 16), 's' -> Letter('s', 14), 't' -> Letter('t', 15)
//    , 'u' -> Letter('u', 11), 'v' -> Letter('v', 6), 'w' -> Letter('w', 6),
//    'x' -> Letter('x', 5), 'y' -> Letter('y', 8), 'z' -> Letter('z', 5))