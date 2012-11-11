package org.prohax.scaml

import org.specs._
import scala.io.Source
import java.io.File
import scala.xml.Xhtml

import example.models._
import java.util.Date

object ScamlParserSpec extends Specification {
  val sampleDir = "src/test/sampledata/"
  val inputDir = sampleDir + "input/"
  val outputDir = sampleDir + "output/"

  def read(filename: String) = Source.fromFile(filename).getLines.mkString
  def read(file: File) = Source.fromFile(file).getLines.mkString

  "The parser" should {
    new File(inputDir).listFiles.foreach((f: File) => {
      "read, parse and output for " + f.getName in {
        val name = f.getName.replaceFirst("\\.scaml$", "")
        val result: Option[ScamlParseResult] = Parser.parse(read(f))
        result.isDefined must beTrue
        result.get.toMethod(name) must beEqualTo(
          read("src/test/sampledata/expected/" + name + ".scala"))
      }
    })
  }
}