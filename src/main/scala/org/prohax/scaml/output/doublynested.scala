package org.prohax.scaml.output

import scala.xml._
import org.prohax.scaml.ScamlFile

object doublynested extends ScamlFile {
  def renderXml() = {
    <html>
      <head>
        <title/>
      </head>
      <body>
        <div/>
      </body>
    </html>
  }
}