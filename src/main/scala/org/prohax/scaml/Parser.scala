package org.prohax.scaml

import scala.util.parsing.combinator._
import syntactical.StdTokenParsers

class Parser extends JavaTokenParsers {
  def plus(p : Parser[~[String, String]]) = p ^^ { case (a ~ b) => a + " " + b }
  def plusOpt(p : Parser[~[Option[String], Option[String]]]) = p ^^ { x => x match {
      case (Some(a) ~ Some(b)) => a + " " + b
      case (Some(a) ~ None) => a
      case (None ~ Some(b)) => b 
      case (None ~ None) => ""
    }
  }
  def plus3(p : Parser[~[~[String, String], String]]) = p ^^ { case ((a ~ b) ~ c) => a + " " + b + " " + c }
  def sum(p : Parser[List[String]], sep : String) = p ^^ { x => x match {
    case Nil => ""
    case xs => xs.reduceLeft(_+sep+_)}
  }

  override val whiteSpace = "".r
  val newline = """[\n\r]+""".r

  def start: Parser[ScamlParseResult] = params ~ rep(line) ^^ {
    case params ~ lines => ScamlParseResult(params, lines)
  }

  def params: Parser[String] = opt(newline ~ "/!\\s*".r ~> ".*".r) ^^ {
    case None => ""
    case Some(x) => x
  }

  def line: Parser[ScamlTag] = newline ~> tagLine

  def notBrace: Parser[String] = "[^{^}]+".r

  // Matches any content between {}, including matched braces.
  def attributes: Parser[String] = "{" ~>
          ((opt(notBrace) ~ (opt(attributes) ^^ (p => p map ("{" + _ + "}"))) ~ opt(notBrace)) ^^ {case (a ~ b ~ c) =>
            (a getOrElse "") + (b getOrElse "") + (c getOrElse "")
          }) <~ "}"

  def tagLine: Parser[ScamlTag] = rep(indent) ~ opt(tag) ~ rep(cls) ~ opt(id) ~ rep(cls) ~ opt(attributes) ~ opt(nontag) ^^
          {case indents ~ tag ~ cls1 ~ id ~ cls2 ~ attributes ~ nontag =>
            new ScamlTag(indents.length, tag, id, cls1 ::: cls2, attributes getOrElse "", nontag)}

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

  def parse(input: String) : Option[ScamlParseResult] = {
    //need a prepended newline here so each line can consume exactly one. There's probably a better way.
    val parsed = parser.parseAll(parser.start, "\n" + input)
    if (parsed.successful) Some(parsed.get) else None
  }
}