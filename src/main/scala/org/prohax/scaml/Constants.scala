package org.prohax.scaml

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
}