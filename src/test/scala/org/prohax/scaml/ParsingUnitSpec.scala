package org.prohax.scaml

import org.specs._

class ParsingUnitSpec extends Specification {
  val parser = new Parser
  "The line parser" should {
    def parse(input: String) = parser.parseAll(parser.line, input)

    "return empty lines" in {
      parse("").successful must beTrue
      parse("").get must beNone
      parse("         ").get must beNone
    }
  }

  "The tagLine parser" should {
    def parse(input: String) = parser.parseAll(parser.tagLine, input)

    "match empty lines but that's ok" in {
      parse("").get.get must beEqualTo(ScamlTag(0, None, None, Nil, None))
    }
    
    "match simple cases" in {
      parse("%html").get.get must beEqualTo(ScamlTag(0, "html"))
      parse("  %h1").get.get must beEqualTo(ScamlTag(1, "h1"))
      parse("          %span").get.get must beEqualTo(ScamlTag(5, "span"))
    }

    "match ids" in {
      parse("%p#header").get.get must beEqualTo(ScamlTag(0, "p", "header"))
      parse("  %p#header#lol").successful must beFalse
      parse("  %html#isok").get.get must beEqualTo(ScamlTag(1, "html", "isok"))
    }

    "match classes" in {
      parse("%p.header").get.get must beEqualTo(ScamlTag(0, "p", None, List("header")))
      parse("  %body.main").get.get must beEqualTo(ScamlTag(1, "body", None, List("main")))
      parse("  %body.main.ima.lol.too").get.get must beEqualTo(ScamlTag(1, "body", None, List("main", "ima", "lol", "too")))
    }

    "match classes and divs" in {
      parse("%p#header.header").get.get must beEqualTo(ScamlTag(0, "p", Some("header"), List("header")))
      parse("  %body#something.main").get.get must beEqualTo(ScamlTag(1, "body", Some("something"), List("main")))
      parse("  %body#is.main.ima.lol.too").get.get must beEqualTo(ScamlTag(1, "body", Some("is"), List("main", "ima", "lol", "too")))
      // needed?
      parse("  %body.main.ima.lol.too#end").get.get must beEqualTo(ScamlTag(1, "body", Some("end"), List("main", "ima", "lol", "too")))
      parse("  %body.main.ima#middle.lol.too").get.get must beEqualTo(ScamlTag(1, "body", Some("middle"), List("main", "ima", "lol", "too")))
    }

    "match divs implicitly" in {
      parse(".header").get.get must beEqualTo(ScamlTag(0, None, None, List("header"), None))
      parse("  #main").get.get must beEqualTo(ScamlTag(1, None, Some("main"), Nil, None))
      parse("    .main.ima#lol.too").get.get must beEqualTo(ScamlTag(2, None, Some("lol"), List("main", "ima", "too"), None))
    }

    "match inline text" in {
      parse("%p.header Header here guys").get.get must beEqualTo(ScamlTag(0, Some("p"), None, List("header"), Some("Header here guys")))
      parse("  %body#main Sup in my body").get.get must beEqualTo(ScamlTag(1, Some("body"), Some("main"), Nil, Some("Sup in my body")))
    }
  }
}