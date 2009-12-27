package org.prohax.scaml

import scala.xml.{TopScope, Xhtml, NodeSeq}

trait ScamlFile {
  def render(): NodeSeq
}

object ScamlFile {
  def render(n: NodeSeq) = {
    val sb = new StringBuilder()
    Xhtml.sequenceToXML(n, TopScope, sb, false, false)
    sb.toString().replaceAll("\\n    ", "\n")
  }
}