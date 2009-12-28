package org.prohax.scaml

import org.specs._

class ConstantsUnitSpec extends Specification {
  "The closing brackets method" should {
    "work for simple cases" in {
      Constants.closingBrackets("(") must beEqualTo(")")
      Constants.closingBrackets("{") must beEqualTo("}")
    }
    "work for multiple brackets" in {
      Constants.closingBrackets("({das((") must beEqualTo("))})")
      Constants.closingBrackets("{{as(ad(dasdadas,43297439{{(ds") must beEqualTo(")}}))}}")
    }
    "work for closed brackets" in {
      Constants.closingBrackets("(a)(b)(c(d{e") must beEqualTo("}))")
      Constants.closingBrackets("""{
      somethign(here {
      """) must beEqualTo("})}")
    }
    "fail when brackets are mismatched" in {
      Constants.closingBrackets("(}") must throwA[RuntimeException]
    }
  }
}