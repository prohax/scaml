package org.prohax.scaml

import org.specs._
import scala.io.Source
import java.io.File

object ScamlParserSpec extends Specification {
  val sampleDir = "src/test/sampledata/"

  "The parser" should {
    new File("src/test/sampledata/input").listFiles.foreach((f: File) => {
      "read, parse and output for " + f.getName in {
        val name = f.getName.replaceFirst("\\.scaml$", "")
        Parser.parse(name, Source.fromFile(f).getLines.mkString) must beEqualTo(
          Source.fromFile("src/main/scala/org/prohax/scaml/output/" + name + ".scala").getLines.mkString)
      }
    })
  }
}