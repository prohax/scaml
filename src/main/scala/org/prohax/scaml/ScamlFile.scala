package org.prohax.scaml

import scala.xml.{TopScope, Xhtml, NodeSeq}

trait ScamlFile {
  def headers = ""

  def renderXml(): NodeSeq

  def renderString() = {
    val sb = new StringBuilder()
    if (!headers.isEmpty) sb append headers + "\n"
    Xhtml.sequenceToXML(renderXml, TopScope, sb, false, false)
    sb.toString().replaceAll("\\n    ", "\n")
  }
}