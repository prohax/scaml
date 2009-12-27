package org.prohax.scaml

case class ScamlTag(level: Int, name: String, id: Option[String], classes: List[String])
object ScamlTag {
  def apply(level: Int, name: String) = new ScamlTag(level, name, None, Nil)

  def apply(level: Int, name: String, id: String) = new ScamlTag(level, name, Some(id), Nil)

  def apply(level: Int, name: String, id: Option[String]) = new ScamlTag(level, name, id, Nil)
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
