package org.prohax.scaml

import org.specs._

object ScamlParserSpec extends Specification {
  "The parser" should {
    "do some simple stuff" in {
      Parser.parse("") must be[ScamlFile]
    }
  }
}