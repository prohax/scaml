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

  def escape(s: String) = "Text(" + Constants.TRIPLE_QUOTES + s + Constants.TRIPLE_QUOTES + ")"
  def indent(indentLevel: Int) = "  " * indentLevel
  def selfClosingTag(indentLevel: Int)(name: String) = indent(indentLevel) + "<" + name + "/>"
  def tagAround(indentLevel: Int, name: String, inner: String, selfClose: Boolean) = if (selfClose && inner.isEmpty) {
    selfClosingTag(indentLevel)(name)
  } else {
    indent(indentLevel) + "<" + name + ">\n" + inner + "\n" + indent(indentLevel) + "</" + name + ">"
  }
  def tagAround(indentLevel: Int, name: String, inner: String): String = tagAround(indentLevel, name, inner, false)
}

case class NestedTag(name: String, subtags: List[NestedTag])

case class ScamlTag(level: Int, name: String)
case class ScamlParseResult(headers: List[String], tags: List[ScamlTag]) {
  def render = {
    println("headers = " + headers)
    (headers.map(Constants.indent(2) + _) ::: (if (tags.isEmpty) List(Constants.indent(2) + Constants.EMPTY) else {
      tags.map((x) => Constants.selfClosingTag(x.level + 2)(x.name))
    })).mkString("\n")
  }
}
object ScamlParseResult {
  def nestTags(tags: List[ScamlTag]) = tags match {
    case Nil => Nil
    case head :: rest => {
      List(recursiveNest(NestedTag(head.name, Nil), head.level, rest)._1)
    }
  }
  private def recursiveNest(parent: NestedTag, parentLevel: Int, remaining: List[ScamlTag]): (NestedTag, List[ScamlTag]) = {
    remaining match {
      case Nil => (parent, Nil)
      case next :: rest => {
        if (next.level > parentLevel) {
          val (child, more) = recursiveNest(NestedTag(next.name, Nil), next.level, rest)
          (NestedTag(parent.name, child :: parent.subtags), more)
        } else {
          (parent, remaining)
        }
      }
    }
  }
}

class Foo extends RegexParsers {
  override val whiteSpace = """[\n\r]*""".r
  def go: Parser[ScamlParseResult] = opt(header) ~ rep(tagLine) ^^ { (x) =>
    ScamlParseResult(x._1.map(List(_)).getOrElse(Nil), x._2)
  }
  def header: Parser[String] = "!!!".r ^^ (_ => Constants.escape(Constants.DOCTYPE))
  def tagLine: Parser[ScamlTag] = rep(indent) ~ tag ^^ { (x) =>
    println("x = " + x)
//    ScamlTag(0, x)
    ScamlTag(x._1.length, x._2)
  }
  def indent: Parser[String] = "  ".r
  def tag: Parser[String] = "%".r ~> tagName
  def tagName: Parser[String] = """\w+""".r
}

object Parser {
  private val foo = new Foo

  def parse(name: String, input: String) = {
    val parsed = foo.parseAll(foo.go, input)
    println("parsed = " + parsed)
    Constants.surround(name, if (parsed.successful) parsed.get.render else Constants.indent(2) + Constants.escape(parsed.toString))
  }
}