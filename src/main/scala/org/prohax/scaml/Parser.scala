package org.prohax.scaml

import scala.util.parsing.combinator._

class Parser extends RegexParsers {
  override val whiteSpace = "".r
  val newline = """[\n\r]+""".r

  def start: Parser[ScamlParseResult] = params ~ opt(header) ~ rep(line) ^^ {
    case params ~ header ~ lines => ScamlParseResult(params, header.map(List(_)).getOrElse(Nil), lines)
  }

  def params: Parser[List[(String, String)]] = opt(newline ~> "/!".r ~> opt(" *".r) ~> repsep(param, ",")) ^^ {
    case None => Nil
    case Some(x) => x
  }

  def param: Parser[(String, String)] = " *".r ~> """\w+""".r ~ " *: *".r ~ """[\w\[\]\(\)]+""".r <~ " *".r ^^ {
    case valName ~ sep ~ typeName => (valName, typeName)
  } 

  def header: Parser[String] = newline ~> "!!!".r ^^ (_ => Constants.DOCTYPE)

  def line: Parser[ScamlTag] = newline ~> tagLine

  def tagLine: Parser[ScamlTag] = rep(indent) ~ opt(tag) ~ rep(cls) ~ opt(id) ~ rep(cls) ~ opt(nontag) ^^
          {case indents ~ tag ~ cls1 ~ id ~ cls2 ~ nontag =>
            new ScamlTag(indents.length, tag, id, cls1 ::: cls2, nontag)}

  def indent: Parser[String] = "  ".r

  def tag: Parser[String] = "%".r ~> word

  def id: Parser[String] = """#""".r ~> word

  def cls: Parser[String] = """\.""".r ~> word

  def word: Parser[String] = """[\w-:]+""".r

  def nontag: Parser[NonTag] = code | text

  def code: Parser[Code] = "= ".r ~> ".*".r ^^ Code

  def text: Parser[Text] = opt(" *".r) ~> """[^\s#\.].*""".r ^^ Text
}

object Parser {
  private val parser = new Parser

  def parse(name: String, imports: List[String], input: String) = {
    //need a prepended newline here so each line can consume exactly one. There's probably a better way.
    val parsed = parser.parseAll(parser.start, "\n" + input)
    if (parsed.successful) parsed.get.render(name, imports) else parsed.toString
  }
}