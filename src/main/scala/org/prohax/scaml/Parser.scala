package org.prohax.scaml

import scala.util.parsing.combinator._

class Parser extends RegexParsers {
  override val whiteSpace = "".r
  val newline = """[\n\r]+""".r

  def start: Parser[ScamlParseResult] = opt(header) ~ rep(line) ^^ {
    case header ~ lines => ScamlParseResult(header.map(List(_)).getOrElse(Nil), lines)
  }

  def header: Parser[String] = newline ~> "!!!".r ^^ (_ => Constants.DOCTYPE)

  def line: Parser[ScamlTag] = newline ~> tagLine

  def tagLine: Parser[ScamlTag] = rep(indent) ~ opt(tag) ~ rep(cls) ~ opt(id) ~ rep(cls) ~ opt(other) ^^
          {case indents ~ tag ~ cls1 ~ id ~ cls2 ~ text =>
            new ScamlTag(indents.length, tag, id, cls1 ::: cls2, text)}

  def indent: Parser[String] = "  ".r

  def tag: Parser[String] = "%".r ~> word

  def word: Parser[String] = """[\w-:]+""".r

  def id: Parser[String] = """#""".r ~> word

  def cls: Parser[String] = """\.""".r ~> word

  def other: Parser[String] = code | text

  def code: Parser[String] = opt(" *".r) ~> "= ".r ~> ".*".r ^^ (code => "{ " + code + " }")

  def text: Parser[String] = opt(" *".r) ~> """[^\s#\.].*""".r
}

object Parser {
  private val parser = new Parser

  def parse(name: String, input: String) = {
    //need a prepended newline here so each line can consume exactly one. There's probably a better way.
    val parsed = parser.parseAll(parser.start, "\n" + input)
    if (parsed.successful) parsed.get.render(name) else parsed.toString
  }
}