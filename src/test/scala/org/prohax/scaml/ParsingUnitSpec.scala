package org.prohax.scaml

import org.specs._

class ParsingUnitSpec extends Specification {
  val parser = new Parser

  "The tagLine parser" should {
    def parse(input: String) = parser.parseAll(parser.tagLine, input)

    "match empty lines but that's ok" in {
      parse("").get must beEqualTo(ScamlTag(0, None, None, Nil, None))
    }

    "match simple cases" in {
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

    "match classes and divs" in {
      parse("%p#header.header").get must beEqualTo(ScamlTag(0, "p", Some("header"), List("header")))
      parse("  %body#something.main").get must beEqualTo(ScamlTag(1, "body", Some("something"), List("main")))
      parse("  %body#is.main.ima.lol.too").get must beEqualTo(ScamlTag(1, "body", Some("is"), List("main", "ima", "lol", "too")))
      // needed?
      parse("  %body.main.ima.lol.too#end").get must beEqualTo(ScamlTag(1, "body", Some("end"), List("main", "ima", "lol", "too")))
      parse("  %body.main.ima#middle.lol.too").get must beEqualTo(ScamlTag(1, "body", Some("middle"), List("main", "ima", "lol", "too")))
    }

    "match divs implicitly" in {
      parse(".header").get must beEqualTo(ScamlTag(0, None, None, List("header"), None))
      parse("  #main").get must beEqualTo(ScamlTag(1, None, Some("main"), Nil, None))
      parse("    .main.ima#lol.too").get must beEqualTo(ScamlTag(2, None, Some("lol"), List("main", "ima", "too"), None))
    }

    "match inline text" in {
      parse("%p.header Header here guys").get must beEqualTo(ScamlTag(0, Some("p"), None, List("header"), Some(Text("Header here guys"))))
      parse("  %body#main Sup in my body").get must beEqualTo(ScamlTag(1, Some("body"), Some("main"), Nil, Some(Text("Sup in my body"))))
    }

    "match block text" in {
      parse("    some text here").get must beEqualTo(ScamlTag(2, None, None, Nil, Some(Text("some text here"))))
    }
  }

  "The 'other' parser" should {
    def parse(input: String) = parser.parseAll(parser.nontag, input)
    "treat code differently" in {
      parse("something").get must beEqualTo(Text("something"))
      parse("          something").get must beEqualTo(Text("something"))
      parse("= 5 * 4").get must beEqualTo(Code("5 * 4"))
    }
    "only parse code if the equals is first" in {
      parse(" = 5 * 4").get must beEqualTo(Text("= 5 * 4"))
      parse("      = 10 * 10").get must beEqualTo(Text("= 10 * 10"))
    }
  }

  "The params parser" should {
    def parse(input: String) = parser.parseAll(parser.params, input)
    "work" in {
      val expected = List(("a", "String"), ("b", "List[String]"))
      parse("\n/! a: String, b: List[String]").get must beEqualTo(expected)
      parse("\n/!    a     :        String      ,      b     :     List[String]     ").get must beEqualTo(expected)
    }
  }

  "A multiline parser" should {
    def parse(input: String) = parser.parseAll(parser.start, input)

    "work for simple cases" in {
      val parsed = parse("""
%html
  %head
    %title""")
      parsed.get must beEqualTo(ScamlParseResult(Nil, Nil, List(
        ScamlTag(0, "html"),
        ScamlTag(1, "head"),
        ScamlTag(2, "title")
        )))
    }

    "handle inline strings" in {
      val parsed = parse("""
%div
  %h1 Hello there.
  %p.byline Lolfase.""")
      parsed.get must beEqualTo(ScamlParseResult(Nil, Nil, List(
        ScamlTag(0, Some("div"), None, Nil, None),
        ScamlTag(1, Some("h1"), None, Nil, Some(Text("Hello there."))),
        ScamlTag(1, Some("p"), None, List("byline"), Some(Text("Lolfase.")))
        )))
    }

    "handle block strings" in {
      val parsed = parse("""
%div
  %h1 Hello there.
    Lolfase.""")
      parsed.get must beEqualTo(ScamlParseResult(Nil, Nil, List(
        ScamlTag(0, Some("div"), None, Nil, None),
        ScamlTag(1, Some("h1"), None, Nil, Some(Text("Hello there."))),
        ScamlTag(2, None, None, Nil, Some(Text("Lolfase.")))
        )))
    }
  }
}