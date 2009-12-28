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
    <p>
      First 9 squares:
      <ul>
        { (1 to 9).map { i =>
          <li>{ i*i }</li>
        } }
      </ul>
    </p>
    <p>
      Nesting times:
      <table>
        { List((1,2), (3,4), (5,6)).map { x =>
          <tr>
            <td>{ x._1 }</td>
            { (1 to x._2).map { i =>
              <td>{ i  }</td>
            } }
          </tr>
        } }
      </table>
    </p>
  }
}