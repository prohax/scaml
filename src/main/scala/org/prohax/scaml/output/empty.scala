package org.prohax.scaml.output

import scala.xml._
import org.prohax.scaml.ScamlFile

object empty extends ScamlFile {
  def render() = {
    Text("")
  }
}