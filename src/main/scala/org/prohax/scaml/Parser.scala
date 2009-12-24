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
}

class Foo extends JavaTokenParsers {
  def go: Parser[String] = header | tag
  def header: Parser[String] = "!!!".r ^^ (_ => "Text(" + Constants.TRIPLE_QUOTES + Constants.DOCTYPE + Constants.TRIPLE_QUOTES + ")")
  def tag: Parser[String] = "%.*".r ^^ (_ => "<html/>")
}

object Parser {
  private val foo = new Foo


  def parse(name: String, input: String) = {
    val parsed = foo.parseAll(foo.go, input)
    println("parsed = " + parsed)
    Constants.surround(name, parsed.getOrElse(Constants.EMPTY))
  }
}