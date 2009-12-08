package org.prohax

import collection.mutable.{HashMap, Map}
import java.lang.String

object Tags {
  case class Tag(ts: List[Tag]) {
    val name = getClass.getSimpleName

    override def toString = if (ts.isEmpty) {
      "<" + name + "></" + name + ">"
    } else {
      "<" + name + ">\n" + ts.map(_.toString).mkString("\n") + "\n</" + name + ">"
    }
  }
  
  case class div() extends Tag(Nil)
  case class html(t: Tag) extends Tag(List(t))
  case class head(t: Tag) extends Tag(List(t))
  case class body(t: Tag) extends Tag(List(t))
}

object SAML {
  import Tags._
  val tags = new HashMap[String, Tag]

  def register(name: String, f: () => Tag) {
    tags += (name -> f())
  }

  def render(name: String) {
    println(tags(name))
  }
}

object Main {
  import Tags._
  def main(args: Array[String]) = {
    SAML.register("simple", () => {
      html(head(div()))
    })

    SAML.render("simple")
  }
}