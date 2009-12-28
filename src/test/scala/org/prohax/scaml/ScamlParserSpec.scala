package org.prohax.scaml

import org.specs._
import scala.io.Source
import java.io.File
import scala.xml.Xhtml

import org.prohax.scaml.models._
import java.util.Date

object ScamlParserSpec extends Specification {
  val sampleDir = "src/test/sampledata/"
  val inputDir = sampleDir + "input/"
  val outputDir = sampleDir + "output/"

  def read(filename: String) = Source.fromFile(filename).getLines.mkString

  "The parser" should {
    new File(inputDir).listFiles.foreach((f: File) => {
      "read, parse and output for " + f.getName in {
        val name = f.getName.replaceFirst("\\.scaml$", "")
        Parser.parse(name, List("org.prohax.scaml.models._"), Source.fromFile(f).getLines.mkString) must beEqualTo(
          read("src/main/scala/org/prohax/scaml/output/" + name + ".scala"))
      }
    })
  }

  "The renderer" should {
    import output._
    List(
      ("classesandids", () => classesandids.renderString(())),
      ("complexnesting", () => complexnesting.renderString(())),
      ("doctype", () => doctype.renderString(())),
      ("doublynested", () => doublynested.renderString(())),
      ("emptyfile", () => emptyfile.renderString(())),
      ("html", () => html.renderString(())),
      ("literals", () => literals.renderString(())),
      ("codes", () => codes.renderString(())),
      ("tag_parsing", () => tag_parsing.renderString(())),
      ("params", () => params.renderString(List(
        Post("Glen", List("This is line 1", "and line 2", "and line 3"), new Date(2009, 12, 28)),
        Post("Ben", List("Lol I am too sick for cider"), new Date(2009, 11, 28)),
        Post("Dylan", List("I", "like", "cider."), new Date(2009, 10, 28))
        )))
    ).foreach(x => {
      "work for " + x._1 in {
        x._2() must beEqualTo(read(outputDir + x._1 + ".html"))
      }
    })
  }
}