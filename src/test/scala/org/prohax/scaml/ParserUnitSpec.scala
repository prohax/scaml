package org.prohax.scaml

import org.specs._

class ParserUnitSpec extends Specification {
  "The nestTags method" should {
    "work for dead simple cases" in {
      ScamlParseResult.nestTags(Nil) must beEmpty
      ScamlParseResult.nestTags(List(ScamlTag(0,"html"))) must beEqualTo(List(NestedTag("html", Nil)))
    }
    "nest once" in {
      ScamlParseResult.nestTags(List(ScamlTag(0,"html"), ScamlTag(1,"head"))) must beEqualTo(
        List(NestedTag("html", List(NestedTag("head", Nil)))))

    }
  }
}