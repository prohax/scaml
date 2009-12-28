package org.prohax.scaml

case class ScamlParseResult(params: List[(String, String)], headers: List[String], tagsIncludingEmpty: List[ScamlTag]) {
  private val tags = tagsIncludingEmpty.filter(!_.isEmpty)
  import ScamlParseResult._
  def render(name: String, imports: List[String]) = """package org.prohax.scaml.output

import scala.xml._
import org.prohax.scaml.ScamlFile
""" + (if (imports.isEmpty) "" else "\n" + imports.map("import " + _ + "\n")) + """
object """ + name + """ extends ScamlFile {""" + renderHeaders + """
  def renderXml""" + methodParams(params) + """
""" + renderBody + """
  }
}"""

  def methodParams(params: List[(String, String)]) = params match {
    case Nil => "(t:Unit) = {"
    case _ => "(t:(" + params.map(_._2) + ") = t match { case (" + params.map(_._1) + ") =>"
  }

  private def renderBody = if (tags.isEmpty) {
    Constants.indent(2) + Constants.EMPTY
  } else {
    nestTags(tags).reverseMap(_.toStringWithIndent(2)).mkString("\n")
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
