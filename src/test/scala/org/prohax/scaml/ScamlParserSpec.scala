package org.prohax.scaml

import org.specs._

object ScamlParserSpec extends Specification {
  def p(s: String) = Parser.parse(s)
  "The parser" should {
    "work with whitespace" in {
      p("") must beEqualTo(Constants.surround(Constants.EMPTY))
      p("           ") must beEqualTo(Constants.surround(Constants.EMPTY))
      p("\n" * 10) must beEqualTo(Constants.surround(Constants.EMPTY))
    }
    "generate xml headers like haml" in {
      p("!!!") must beEqualTo(Constants.surround(Constants.DOCTYPE))
    }
  }
}