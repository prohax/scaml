package org.prohax.scaml.output

import scala.xml._
import org.prohax.scaml.ScamlFile

object simplenested extends ScamlFile {
  def render() = {
    <html>
      <body/>
    </html>
  }
}