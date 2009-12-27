package org.prohax.scaml.output

import scala.xml._
import org.prohax.scaml.ScamlFile

object classesandids extends ScamlFile {
  def render() = {
    <html>
      <head>
        <title/>
      </head>
      <body>
        <div id='first'>
          <h1 class='megaBig'/>
          <div id='name' class='super win'>
            <p class='thin'>
              <strong/>
            </p>
            <p class='wide'/>
            <br/>
          </div>
        </div>
        <div id='last'/>
      </body>
    </html>
  }
}