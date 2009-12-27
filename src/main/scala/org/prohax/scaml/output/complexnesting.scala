package org.prohax.scaml.output

import scala.xml._
import org.prohax.scaml.ScamlFile

object complexnesting extends ScamlFile {
  def renderXml() = {
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