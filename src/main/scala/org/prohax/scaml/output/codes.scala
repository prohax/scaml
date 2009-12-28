package org.prohax.scaml.output

import scala.xml._
import org.prohax.scaml.ScamlFile

object codes extends ScamlFile {
  def renderXml() = {
    <p>
      Counting to three:
      <ul>
        { (1 to 3).map(i =>
          <li>{ i }</li>
        ) }
      </ul>
    </p>
  }
}