package org.prohax.scaml.output

import scala.xml._
import org.prohax.scaml.ScamlFile

import org.prohax.scaml.models._

object complexnesting extends ScamlFile[Unit] {
  def renderXml(t:Unit) = {
    <html>
      <head>
        <title/>
      </head>
      <body>
        <div>
          <h1/>
          <div>
            <p>
              <strong/>
            </p>
            <p/>
          </div>
        </div>
        <div/>
      </body>
    </html>
  }
}