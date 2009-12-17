package org.prohax.scaml.views

import scala.xml._
import org.prohax.scaml.ScamlFile

object empty extends ScamlFile {
  def render() = Text("")
}