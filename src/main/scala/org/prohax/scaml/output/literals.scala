package org.prohax.scaml.output

import scala.xml._
import org.prohax.scaml.ScamlFile

import org.prohax.scaml.models._

object literals extends ScamlFile[Unit] {
  def renderXml(t:Unit) = {
    <div>
      <h1>Test.</h1>
      <p>
        Welcome, Mr
        <span class='bold'>Boldfase</span>
      </p>
      <p>
        You are
        <span class='red'>{ 20 + 5 }</span>
        years old.
      </p>
      <p>
        And
        { 6 * 30.5 }
        cm tall.
      </p>
    </div>
  }
}