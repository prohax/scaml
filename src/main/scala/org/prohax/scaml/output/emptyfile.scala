package org.prohax.scaml.output

import scala.xml._
import org.prohax.scaml.ScamlFile

object emptyfile extends ScamlFile {
  def renderXml() = {
    Text("")
  }
}