package org.prohax.scaml

import org.specs._

class ParserUnitSpec extends Specification {
  val a = ScamlTag(0, "html")
  val b = ScamlTag(1, "head")
  val c = ScamlTag(2, "title")
  val d = ScamlTag(1, "body")
  val e = ScamlTag(2, "h1")
  
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
    "handle multiple at sub levels" in {
      ScamlParseResult.nestTags(List(a, b, d)) must beEqualTo(List(
        NestedTag("html", List(
          NestedTag("body", Nil),
          NestedTag("head", Nil)))))
      ScamlParseResult.nestTags(List(a, b, c, d, e)) must beEqualTo(List(
        NestedTag("html", List(
          NestedTag("body", List(
            NestedTag("h1", Nil))),
          NestedTag("head", List(
            NestedTag("title", Nil)))))))
    }
    "handle multiples at base level" in {
      ScamlParseResult.nestTags(List(a, b, ScamlTag(0, "lol"))) must beEqualTo(
        List(NestedTag("lol", Nil),
        NestedTag("html", List(
          NestedTag("head", Nil)))))
    }
  }
}