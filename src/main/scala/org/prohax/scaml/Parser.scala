package org.prohax.scaml

import scala.xml.{Text, NodeSeq, Unparsed}
import scala.util.parsing.combinator._

object Constants {
  val TRIPLE_QUOTES = "\"\"\""
  val DOCTYPE = """<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">"""
  val EMPTY = """Text("")"""

  def surround(name: String,body: => String) = """package org.prohax.scaml.output

import scala.xml._
import org.prohax.scaml.ScamlFile

object """ + name + """ extends ScamlFile {
  def render() = {
    """ + body + """
  }
}"""

  def selfClosingTag(name: String) = "<" + name + "/>"
  def emptyTag(name: String) = tagAround(name, "")
  def tagAround(name: String, inner: String, selfClose: Boolean) = if (selfClose && inner.isEmpty) {
    selfClosingTag(name)
  } else {
    "<" + name + ">" + inner + "</" + name + ">"
  }
  def tagAround(name: String, inner: String): String = tagAround(name, inner, false)
}

class Foo extends RegexParsers {
  override val whiteSpace = "".r
  def go: Parser[List[String]] = header | rep1(tag)
  def header: Parser[List[String]] = "!!!".r ^^ (_ => List("Text(" + Constants.TRIPLE_QUOTES + Constants.DOCTYPE + Constants.TRIPLE_QUOTES + ")"))
  def tag: Parser[String] = "^%".r ~> closedTag ~ rep(subtag) ^^ ((x) => Constants.tagAround(x._1, x._2.mkString, true))
  def closedTag: Parser[String] = ".*".r
  def subtag: Parser[String] = """\n  %""".r ~> ".*".r ^^ Constants.selfClosingTag
}

object Parser {
  private val foo = new Foo

  def parse(name: String, input: String) = {
    val parsed = foo.parseAll(foo.go, input).getOrElse(List(Constants.EMPTY))
    println("parsed = " + parsed)
    Constants.surround(name, parsed.mkString)
  }
}