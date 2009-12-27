package org.prohax.scaml

case class ScamlTag(level: Int, name: Option[String], id: Option[String], classes: List[String], text: Option[String]) {
  def isEmpty = name.isEmpty && id.isEmpty && classes.isEmpty && text.isEmpty
}
object ScamlTag {
  def apply(level: Int, name: String) = new ScamlTag(level, Some(name), None, Nil, None)

  def apply(level: Int, name: String, id: String) = new ScamlTag(level, Some(name), Some(id), Nil, None)

  def apply(level: Int, name: String, id: Option[String]) = new ScamlTag(level, Some(name), id, Nil, None)
  
  def apply(level: Int, name: String, id: Option[String], classes: List[String]) = new ScamlTag(level, Some(name), id, classes, None)
}

case class NestedTag(tag: ScamlTag, subtags: List[NestedTag]) {
  def toStringWithIndent(indent: Int): String = if (tag.isEmpty) "" else Constants.indent(indent) + {
    val name = tag.name.getOrElse("div")
    if (subtags.isEmpty) "<" + name + attrs + "/>" else {
      "<" + name + attrs + ">\n" + subtags.reverseMap(_.toStringWithIndent(indent + 1)).mkString("\n") + "\n" + Constants.indent(indent) + "</" + name + ">"
    }
  }

  private def attrs = tag.id.map(id => " id='" + id + "'").getOrElse("") + (tag.classes match {
    case Nil => ""
    case classes => " class='" + classes.mkString(" ") + "'"
  })
}
