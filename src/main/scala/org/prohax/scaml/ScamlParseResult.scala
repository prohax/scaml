package org.prohax.scaml

case class ScamlTag(level: Int, name: String, id: Option[String], classes: List[String])
object ScamlTag {
  def apply(level: Int, name: String) = new ScamlTag(level, name, None, Nil)
  def apply(level: Int, name: String, id: String) = new ScamlTag(level, name, Some(id), Nil)
  def apply(level: Int, name: String, id: Option[String]) = new ScamlTag(level, name, id, Nil)
}

case class ScamlParseResult(headers: List[String], tags: List[ScamlTag]) {
  import ScamlParseResult._
  def render = {
    val tagLines = if (tags.isEmpty) List(Constants.indent(2) + Constants.EMPTY) else {
      nestTags(tags).map(_.toStringWithIndent(2))
    }
    (headers.map(Constants.indent(2) + _) ::: tagLines).mkString("\n")
  }
}

object ScamlParseResult {
  def nestTags(tags: List[ScamlTag]) = recursiveNest(NestedTag("", Nil), Math.MIN_INT, tags)._1.subtags

  private def recursiveNest(parent: NestedTag, parentLevel: Int, remaining: List[ScamlTag]): (NestedTag, List[ScamlTag]) = remaining match {
    case Nil => (parent, Nil)
    case next :: rest => if (next.level > parentLevel) {
      val (child, more) = recursiveNest(NestedTag(next.name, Nil), next.level, rest)
      recursiveNest(NestedTag(parent.name, child :: parent.subtags), parentLevel, more)
    } else {
      (parent, remaining)
    }
  }
}

case class NestedTag(name: String, subtags: List[NestedTag]) {
  def toStringWithIndent(indent: Int): String = Constants.indent(indent) + {
    if (subtags.isEmpty) "<" + name + "/>" else {
      "<" + name + ">\n" + subtags.reverseMap(_.toStringWithIndent(indent + 1)).mkString("\n") + "\n" + Constants.indent(indent) + "</" + name + ">"
    }
  }
}
