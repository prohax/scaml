package org.prohax.scaml

import scala.xml.{Text, NodeSeq}

trait ScamlFile {
  def render: NodeSeq
}

object Parser {
  def parse(input: String): ScamlFile = new ScamlFile { def render = Text("")}
}