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
  def emptyTag(name: String) = "<" + name + "></" + name + ">"
}

class Foo extends RegexParsers {
  def go: Parser[List[String]] = header | rep1(tag) ^^ ((x: List[List[String]]) => x.flatten)
  def header: Parser[List[String]] = "!!!".r ^^ (_ => List("Text(" + Constants.TRIPLE_QUOTES + Constants.DOCTYPE + Constants.TRIPLE_QUOTES + ")"))
  def tag: Parser[List[String]] = "^%".r ~> closedTag ~ rep(subtag) ^^ ((x) => x._1 :: x._2)
  def closedTag: Parser[String] = ".*".r ^^ Constants.selfClosingTag
  def subtag: Parser[String] = "\\.\\.%".r ~> ".*".r ^^ Constants.emptyTag
}

object Parser {
  private val foo = new Foo

  def parse(name: String, input: String) = {
    val parsed = foo.parseAll(foo.go, input).getOrElse(List(Constants.EMPTY))
    println("parsed = " + parsed)
    Constants.surround(name, parsed.mkString)
  }
}