package org.prohax.scaml.output

import scala.xml._
import org.prohax.scaml.ScamlFile

import org.prohax.scaml.models._

object singlenested extends ScamlFile[Unit] {
  def renderXml(t:Unit) = {
    <html>
      <head/>
      <body/>
    </html>
  }
}