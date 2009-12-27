package org.prohax.scaml

case class ScamlParseResult(headers: List[String], tags: List[ScamlTag]) {
  import ScamlParseResult._
  def render(name: String) = """package org.prohax.scaml.output

import scala.xml._
import org.prohax.scaml.ScamlFile

object """ + name + """ extends ScamlFile {""" + renderHeaders + """
  def renderXml() = {
""" + renderBody + """
  }
}"""

  private def renderBody = if (tags.isEmpty || tags.find(!_.isEmpty).isEmpty) {
    Constants.indent(2) + Constants.EMPTY
  } else {
    nestTags(tags).map(_.toStringWithIndent(2)).mkString("\n")
  }

  private def renderHeaders = headers match {
    case Nil => ""
    case _ => """
  override def headers = """ + Constants.TRIPLE_QUOTES + headers.mkString("\n") + Constants.TRIPLE_QUOTES + "\n"
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
