package org.prohax.scaml

case class ScamlTag(level: Int, name: Option[String], id: Option[String], classes: List[String], text: Option[NonTag]) {
  def isEmpty = !isTag && text.isEmpty

  def isText = !isTag && !text.isEmpty && text.get.isInstanceOf[Text]

  def isCode = !isTag && !text.isEmpty && text.get.isInstanceOf[Code]

  def isTag = !(name.isEmpty && id.isEmpty && classes.isEmpty)
}
object ScamlTag {
  def apply(level: Int, name: String) = new ScamlTag(level, Some(name), None, Nil, None)

  def apply(level: Int, name: String, id: String) = new ScamlTag(level, Some(name), Some(id), Nil, None)

  def apply(level: Int, name: String, id: Option[String]) = new ScamlTag(level, Some(name), id, Nil, None)

  def apply(level: Int, name: String, id: Option[String], classes: List[String]) = new ScamlTag(level, Some(name), id, classes, None)
}

case class NestedTag(tag: ScamlTag, subtags: List[NestedTag]) {
  def toStringWithIndent(indent: Int): String = {
    if (tag.isEmpty) {
      ""
    } else {
      Constants.indent(indent) + {
        if (tag.isText) {
          tag.text.get.s
        } else if (tag.isCode) {
          val code = tag.text.get.s
          "{ " + code + {
            if (subtags.isEmpty) "" else
              "\n" + subtags.reverseMap(_.toStringWithIndent(indent + 1)).mkString("\n") + "\n" + Constants.indent(indent)
          } + Constants.closingBrackets(code) + " }"
        } else {
          val name = tag.name.getOrElse("div")
          val within = tag.text.map(_.toInlineString).getOrElse({
            if (subtags.isEmpty) "" else
              "\n" + subtags.reverseMap(_.toStringWithIndent(indent + 1)).mkString("\n") + "\n" + Constants.indent(indent)
          })
          if (within.isEmpty) "<" + name + attrs + "/>" else {
            "<" + name + attrs + ">" + within + "</" + name + ">"
          }
        }
      }
    }
  }

  private def attrs = tag.id.map(id => " id='" + id + "'").getOrElse("") + (tag.classes match {
    case Nil => ""
    case classes => " class='" + classes.mkString(" ") + "'"
  })
}

sealed abstract case class NonTag(s: String) {
  def toInlineString: String
}
case class Text(text: String) extends NonTag(text) {
  def toInlineString = s
}
case class Code(code: String) extends NonTag(code) {
  def toInlineString = "{ " + s + " }"  
}