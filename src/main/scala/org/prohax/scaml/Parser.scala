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

  def indent(indentLevel: Int) = "  " * indentLevel
  def selfClosingTag(indentLevel: Int)(name: String) = indent(indentLevel) + "<" + name + "/>"
  def tagAround(indentLevel: Int, name: String, inner: String, selfClose: Boolean) = if (selfClose && inner.isEmpty) {
    selfClosingTag(indentLevel)(name)
  } else {
    indent(indentLevel) + "<" + name + ">\n" + inner + "\n" + indent(indentLevel) + "</" + name + ">"
  }
  def tagAround(indentLevel: Int, name: String, inner: String): String = tagAround(indentLevel, name, inner, false)
}

class Foo extends RegexParsers {
  override val whiteSpace = "".r
  def go: Parser[List[String]] = header | rep1(tag)
  def header: Parser[List[String]] = "!!!".r ^^ (_ => List(Constants.indent(2) + "Text(" + Constants.TRIPLE_QUOTES + Constants.DOCTYPE + Constants.TRIPLE_QUOTES + ")"))
  def tag: Parser[String] = "^%".r ~> tagName ~ rep(subtag) ^^ ((x) => Constants.tagAround(2, x._1, x._2.mkString("\n"), true))
  def tagName: Parser[String] = """\w+""".r
  def subtag: Parser[String] = """\n  %""".r ~> tagName ^^ Constants.selfClosingTag(3)
}

object Parser {
  private val foo = new Foo

  def parse(name: String, input: String) = {
    val parsed = foo.parseAll(foo.go, input).getOrElse(List(Constants.indent(2) + Constants.EMPTY))
    println("parsed = " + parsed)
    Constants.surround(name, parsed.mkString)
  }
}