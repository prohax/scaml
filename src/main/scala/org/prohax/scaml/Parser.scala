package org.prohax.scaml

import scala.util.parsing.combinator._

class Parser extends RegexParsers {
  override val whiteSpace = """[\n\r]*""".r
  def start: Parser[ScamlParseResult] = opt(header) ~ rep(tagLine) ^^ (x =>
    ScamlParseResult(x._1.map(List(_)).getOrElse(Nil), x._2))
  def header: Parser[String] = "!!!".r ^^ (_ => Constants.escape(Constants.DOCTYPE))
  def tagLine: Parser[ScamlTag] = rep(indent) ~ tag ~ opt(id) ^^ (x => ScamlTag(x._1._1.length, x._1._2, x._2))
  def indent: Parser[String] = "  ".r
  def tag: Parser[String] = "%".r ~> tagName
  def tagName: Parser[String] = """\w+""".r
  def id: Parser[String] = "#".r ~> tagName
}

object Parser {
  private val parser = new Parser

  def parse(name: String, input: String) = {
    val parsed = parser.parseAll(parser.start, input)
    println("parsed = " + parsed)
    Constants.surround(name, if (parsed.successful) parsed.get.render else Constants.indent(2) + Constants.escape(parsed.toString))
  }
}