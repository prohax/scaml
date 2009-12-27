package org.prohax.scaml

import org.specs._
import scala.io.Source
import java.io.File
import scala.xml.Xhtml

object ScamlParserSpec extends Specification {
  val sampleDir = "src/test/sampledata/"
  val inputDir = sampleDir + "input/"
  val outputDir = sampleDir + "output/"

  def read(filename: String) = Source.fromFile(filename).getLines.mkString

  "The parser" should {
    new File(inputDir).listFiles.foreach((f: File) => {
      "read, parse and output for " + f.getName in {
        val name = f.getName.replaceFirst("\\.scaml$", "")
        Parser.parse(name, Source.fromFile(f).getLines.mkString) must beEqualTo(
          read("src/main/scala/org/prohax/scaml/output/" + name + ".scala"))
      }
    })
  }

  "The renderer" should {
    import output._
    List(
      ("classesandids", () => classesandids.renderString),
      ("complexnesting", () => complexnesting.renderString),
      ("doctype", () => doctype.renderString),
      ("doublynested", () => doublynested.renderString),
      ("emptyfile", () => emptyfile.renderString),
      ("html", () => html.renderString),
      ("literals", () => literals.renderString)
    ).foreach(x => {
      "work for " + x._1 in {
        x._2() must beEqualTo(read(outputDir + x._1 + ".html"))
      }
    })
  }
}