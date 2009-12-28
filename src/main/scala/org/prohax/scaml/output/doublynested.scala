package org.prohax.scaml.output

import scala.xml._
import org.prohax.scaml.ScamlFile

import org.prohax.scaml.models._

object doublynested extends ScamlFile[Unit] {
  def renderXml(t:Unit) = {
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