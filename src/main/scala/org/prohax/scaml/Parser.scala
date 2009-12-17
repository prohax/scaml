package org.prohax.scaml

import scala.xml.{Text, NodeSeq, Unparsed}
import scala.util.parsing.combinator._

object Constants {
  val DOCTYPE = """<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">"""
}

object Parser {
  private val foo = new Foo

  def parse(input: String) = foo.parseAll(foo.header, input).getOrElse("")
}

class Foo extends JavaTokenParsers {
  def header: Parser[String] = "!!!".r ^^ (_ => Constants.DOCTYPE)
}