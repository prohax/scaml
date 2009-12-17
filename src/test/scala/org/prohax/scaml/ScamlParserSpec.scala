package org.prohax.scaml

import org.specs._
import scala.io.Source

object ScamlParserSpec extends Specification {
  def p(s: String) = Parser.parse(s)
  "The parser" should {
    "work with whitespace" in {
      val emptyOutput = Source.fromFile("src/test/sampledata/output/empty.scala").getLines.mkString
      p("") must beEqualTo(emptyOutput)
      p("           ") must beEqualTo(emptyOutput)
      p("\n" * 10) must beEqualTo(emptyOutput)
    }
    "generate xml headers like haml" in {
      p("!!!") must beEqualTo(Constants.surround(Constants.DOCTYPE))
    }
  }
}