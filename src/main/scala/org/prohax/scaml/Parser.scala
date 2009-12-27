package org.prohax.scaml

import scala.util.parsing.combinator._

class Parser extends RegexParsers {
  override val whiteSpace = """[\n\r]*""".r
  def start: Parser[ScamlParseResult] = opt(header) ~ rep(tagLine) ^^ (x =>
    ScamlParseResult(x._1.map(List(_)).getOrElse(Nil), x._2))
  def header: Parser[String] = "!!!".r ^^ (_ => Constants.DOCTYPE)
  def tagLine: Parser[ScamlTag] = rep(indent) ~ tag ~ rep(cls) ~ opt(id) ~ rep(cls) ^^
          { case indents ~ tag ~ cls1 ~ id ~ cls2 => ScamlTag(indents.length,tag,id,cls1 ::: cls2) }
  def indent: Parser[String] = "  ".r
  def tag: Parser[String] = "%".r ~> word
  def word: Parser[String] = """\w+""".r
  def id: Parser[String] = """#""".r ~> word
  def cls: Parser[String] = """\.""".r ~> word
}

object Parser {
  private val parser = new Parser

  def parse(name: String, input: String) = {
    val parsed = parser.parseAll(parser.start, input)
    if (parsed.successful) parsed.get.render(name) else parsed.toString
  }
}