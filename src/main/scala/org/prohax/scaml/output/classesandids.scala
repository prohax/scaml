package org.prohax.scaml.output

import scala.xml._
import org.prohax.scaml.ScamlFile

object classesandids extends ScamlFile {
  override def headers = """<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">"""

  def renderXml() = {
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