package org.prohax.scaml

object Constants {
  val TRIPLE_QUOTES = "\"\"\""

  def indent(indentLevel: Int) = "  " * indentLevel

  def closingBrackets(s: String) = findUnclosedBrackets("", s.toCharArray.toList).toCharArray.toList.reverseMap(closers(_)).mkString

  private val closers = Map('(' -> ')', '{' -> '}')
  private val openers = Map(')' -> '(', '}' -> '{')

  private def findUnclosedBrackets(found: String, remaining: List[Char]): String = remaining match {
    case Nil => found
    case next :: rest => {
      if (closers.contains(next)) {
        findUnclosedBrackets(found + next, rest)
      } else if (openers.contains(next)) {
        if (found.toCharArray.last == openers(next)) {
          findUnclosedBrackets(found.substring(0, found.length - 1), rest)
        } else {
          throw new RuntimeException("Detected char " + next + " as closing bracket, but last opened bracket was " + found.toCharArray.last)
        }
      } else {
        findUnclosedBrackets(found, rest)
      }
    }
  }
}