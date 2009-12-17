package org.prohax.scaml

import scala.xml.NodeSeq

trait ScamlFile {
  def render(): NodeSeq
}