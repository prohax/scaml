package org.prohax.scaml

import org.specs._

class ParserUnitSpec extends Specification {
  val a = ScamlTag(0, "html")
  val b = ScamlTag(1, "head")
  val c = ScamlTag(2, "title")
  
  "The nestTags method" should {
    "work for dead simple cases" in {
      ScamlParseResult.nestTags(Nil) must beEmpty
      ScamlParseResult.nestTags(List(a)) must beEqualTo(List(NestedTag("html", Nil)))
    }
    "nest simply" in {
      ScamlParseResult.nestTags(List(a, b)) must beEqualTo(List(
        NestedTag("html", List(
          NestedTag("head", Nil)))))
      ScamlParseResult.nestTags(List(a, b, c)) must beEqualTo(List(
        NestedTag("html", List(
          NestedTag("head", List(
            NestedTag("title", Nil)))))))
    }
  }
}