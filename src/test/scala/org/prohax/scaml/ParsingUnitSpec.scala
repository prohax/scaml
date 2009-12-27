package org.prohax.scaml

import org.specs._

class ParsingUnitSpec extends Specification {
  val parser = new Parser
  "The tagLine parser" should {

    def parse(input: String) = parser.parseAll(parser.tagLine, input)

    "match simple cases" in {
      parse("").successful must beFalse
      parse("%html").get must beEqualTo(ScamlTag(0, "html"))
      parse("  %h1").get must beEqualTo(ScamlTag(1, "h1"))
      parse("          %span").get must beEqualTo(ScamlTag(5, "span"))
    }

    "match ids" in {
      parse("%p#header").get must beEqualTo(ScamlTag(0, "p", "header"))
      parse("  %p#header#lol").successful must beFalse
      parse("  %html#isok").get must beEqualTo(ScamlTag(1, "html", "isok"))
    }

    "match classes" in {
      parse("%p.header").get must beEqualTo(ScamlTag(0, "p", None, List("header")))
      parse("  %body.main").get must beEqualTo(ScamlTag(1, "body", None, List("main")))
      parse("  %body.main.ima.lol.too").get must beEqualTo(ScamlTag(1, "body", None, List("main", "ima", "lol", "too")))
    }
  }
}