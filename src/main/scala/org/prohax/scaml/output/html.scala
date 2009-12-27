package org.prohax.scaml.output

import scala.xml._
import org.prohax.scaml.ScamlFile

object html extends ScamlFile {
  def renderXml() = {
    <html/>
  }
}