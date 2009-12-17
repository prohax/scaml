package org.prohax.scaml

import org.specs._
import scala.io.Source
import java.io.File

object ScamlParserSpec extends Specification {
  val sampleDir = "src/test/sampledata/"

  def p(s: String) = Parser.parse(s)
  "The parser" should {
    "work with whitespace" in {
      val emptyOutput = Source.fromFile(sampleDir + "output/empty.scala").getLines.mkString
      p("") must beEqualTo(emptyOutput)
      p("           ") must beEqualTo(emptyOutput)
      p("\n" * 10) must beEqualTo(emptyOutput)
    }
    new File(sampleDir + "input").listFiles.foreach((f: File) => {
      "should read and output for " + f.getName in {
        p(Source.fromFile(f).getLines.mkString) must beEqualTo(Source.fromFile(sampleDir + "output/" + f.getName.replaceFirst("\\.scaml$", ".scala")).getLines.mkString)
      }
    })
  }
}