package org.prohax.scaml

import org.specs.Specification


class AttributeEnhancementSpec extends Specification {
  def check(content: String, add: List[(String, String)], expected: String) = {
    Util.addIfMissing(content, add) must_== expected
  }

  "Adding missing attributes" should {
    List(
      () => check("", List(("class" -> "cls1")), "class=\"cls1\""),
      () => check("class=\"foo\"", List(("class" -> "cls1")), "class=\"foo\""),
      () => check("class={null: String}", List(("class" -> "cls1")), "class={Option({null: String}) getOrElse \"cls1\"}"),
      () => check("class={if (true) { null } else { value }}", List(("class" -> "cls1")),
        "class={Option({if (true) { null } else { value }}) getOrElse \"cls1\"}")
      // TODO: whitespace around the '='
    ).foreach { f => "xxx" in { f() } } 
  }
}