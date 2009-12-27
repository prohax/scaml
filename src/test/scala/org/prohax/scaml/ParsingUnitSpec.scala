package org.prohax.scaml

import org.specs._

class ParsingUnitSpec extends Specification {
  val parser = new Parser
  "The tagLine parser" should {
    def parse(input: String) = parser.parseAll(parser.tagLine, input)
    "match simple cases" in {
      parse("").successful must beFalse
    }
  }
}