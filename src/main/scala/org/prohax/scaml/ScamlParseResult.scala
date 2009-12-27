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

case class NestedTag(tag: ScamlTag, subtags: List[NestedTag]) {
  def toStringWithIndent(indent: Int): String = Constants.indent(indent) + {
    if (subtags.isEmpty) "<" + tag.name + attrs + "/>" else {
      "<" + tag.name + attrs + ">\n" + subtags.reverseMap(_.toStringWithIndent(indent + 1)).mkString("\n") + "\n" + Constants.indent(indent) + "</" + tag.name + ">"
    }
  }

  private def attrs = tag.id.map(id => " id='" + id + "'").getOrElse("") + (tag.classes match {
    case Nil => ""
    case classes => " class='" + classes.mkString(" ") + "'"
  })
}
