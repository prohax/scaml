package org.prohax.scaml

case class ScamlParseResult(params: String, tagsIncludingEmpty: List[ScamlTag]) {
  private val tags = tagsIncludingEmpty.filter(!_.isEmpty)

  import ScamlParseResult._

  def toMethod(name: String) = "def " + name + methodParams + " = {\n" + renderBody + "\n}"

  def methodParams = if (params.isEmpty) "" else "(" + params + ")"

  private def renderBody = if (tags.isEmpty) {
    ""
  } else {
    nestTags(tags).reverseMap(_.toStringWithIndent(2)).mkString("\n")
  }
}

object ScamlParseResult {
  def nestTags(tags: List[ScamlTag]) = recursiveNest(NestedTag(ScamlTag(Math.MIN_INT, ""), Nil), tags)._1.subtags

  private def recursiveNest(parent: NestedTag, remaining: List[ScamlTag]): (NestedTag, List[ScamlTag]) = remaining match {
    case Nil => (parent, Nil)
    case next :: rest => if (next.level > parent.tag.level) {
      val (child, more) = recursiveNest(NestedTag(next, Nil), rest)
      recursiveNest(NestedTag(parent.tag, child :: parent.subtags), more)
    } else {
      (parent, remaining)
    }
  }
}
