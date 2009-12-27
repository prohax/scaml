package org.prohax.scaml

import org.specs._

class NestingTagsSpec extends Specification {
  val a = ScamlTag(0, "html")
  val b = ScamlTag(1, "head")
  val c = ScamlTag(2, "title")
  val d = ScamlTag(1, "body")
  val e = ScamlTag(2, "h1")
  
  "The nestTags method" should {
    "work for dead simple cases" in {
      ScamlParseResult.nestTags(Nil) must beEmpty
      ScamlParseResult.nestTags(List(a)) must beEqualTo(List(NestedTag(a, Nil)))
    }
    "nest simply" in {
      ScamlParseResult.nestTags(List(a, b)) must beEqualTo(List(
        NestedTag(a, List(
          NestedTag(b, Nil)))))
      ScamlParseResult.nestTags(List(a, b, c)) must beEqualTo(List(
        NestedTag(a, List(
          NestedTag(b, List(
            NestedTag(c, Nil)))))))
    }
    "handle multiple at sub levels" in {
      ScamlParseResult.nestTags(List(a, b, d)) must beEqualTo(List(
        NestedTag(a, List(
          NestedTag(d, Nil),
          NestedTag(b, Nil)))))
      ScamlParseResult.nestTags(List(a, b, c, d, e)) must beEqualTo(List(
        NestedTag(a, List(
          NestedTag(d, List(
            NestedTag(e, Nil))),
          NestedTag(b, List(
            NestedTag(c, Nil)))))))
    }
    "handle multiples at base level" in {
      val lol = ScamlTag(0, "lol")
      ScamlParseResult.nestTags(List(a, b, lol)) must beEqualTo(
        List(NestedTag(lol, Nil),
        NestedTag(a, List(
          NestedTag(b, Nil)))))
    }
  }
}