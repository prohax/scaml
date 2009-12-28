package org.prohax.scaml

import scala.xml.{TopScope, Xhtml, NodeSeq}

trait ScamlFile[T] {
  def headers = ""

  def renderXml(t: T): NodeSeq

  def renderString(t: T) = {
    val sb = new StringBuilder()
    if (!headers.isEmpty) sb append headers + "\n"
    Xhtml.sequenceToXML(renderXml(t), TopScope, sb, false, false)
    sb.toString().replaceAll("\\n    ", "\n")
  }
}