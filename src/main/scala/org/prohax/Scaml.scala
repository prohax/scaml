package org.prohax

import collection.mutable.ArrayBuffer

object Scaml {
  trait Tag {
    val attrs: Map[Symbol, String]
    val tags: Seq[Tag]
    val name: String

    override def toString = {
      val tagOpen = "<" + name + attrs.map(kv => " " + kv._1.name + "='" + kv._2.replaceAll("'", "\\\\'") + "'").mkString + ">"
      if (tags.isEmpty) {
        tagOpen + "</" + name + ">"
      } else {
        "\n" + tagOpen + tags.map(_.toString).mkString + "</" + name + ">\n"
      }
    }
  }

  trait NamedByClass {
    val name = getClass.getSimpleName
  }

  case class StringTag(name: String) extends Tag {
    val attrs = Map[Symbol, String]()
    val tags = Nil

    override def toString = name
  }
  case class TagFromString(n: String) {
    def apply(f: Function1[ArrayBuffer[Tag], ArrayBuffer[Tag]])(implicit ab: ArrayBuffer[Tag]) = {
      val me = new Tag {
        val attrs = Map[Symbol, String]()
        val name = n
        val tags = List()
      }
      
      (ab2: ArrayBuffer[Tag]) => {
        ab2 append me
        ab2
      }
    }

    def apply(s: String)(implicit ab: ArrayBuffer[Tag]) = (ab2: ArrayBuffer[Tag]) => {
      ab2 append StringTag(s)
      ab2
    }
  }

  implicit def stringWithTags(s: String) = TagFromString(s)

  implicit def stringToTag(s: String) = StringTag(s)

  case class TagBuilder(n: String) {
    def apply(as: Map[Symbol, String], ts: Seq[Tag]) = new Tag {
      val attrs: Map[Symbol, String] = as
      val name = n
      val tags: Seq[Tag] = ts
    }

    def apply(as: Map[Symbol, String], ts: Tag*): Tag = apply(as, ts)

    def apply(): Tag = apply(Map[Symbol, String]())

    def apply(ts: Tag*): Tag = apply(Map[Symbol, String](), ts)

    def apply(a1: (Symbol, String), ts: Tag*): Tag = apply(Map(a1), ts)

    def apply(a1: (Symbol, String), a2: (Symbol, String), ts: Tag*): Tag = apply(Map(a1, a2), ts)

    def apply(a1: (Symbol, String), a2: (Symbol, String), a3: (Symbol, String), ts: Tag*): Tag = apply(Map(a1, a2, a3), ts)
  }

  val List(html, head, title, body, h1, h2, p, span, div) = List(
    "html", "head", "title", "body", "h1", "h2", "p", "span", "div"
    ).map(TagBuilder(_))
}

object NewScaml {
  import scala.xml.{TopScope, Elem, UnprefixedAttribute, Text}

  private def attributes(classes: List[String], id: Option[String]) = (classes, id) match {
    case (Nil, None) => scala.xml.Null
    case (classes, id) => new UnprefixedAttribute("class", classes.map(Text(_)), id match {
      case None => scala.xml.Null
      case Some(s) => new UnprefixedAttribute("id", Text(s), scala.xml.Null)
    })
  }

  case class SymbolTag(classes: List[String], id: Option[String], name: String) {
    val elem = Elem(null, name, attributes(classes, id), TopScope)

    override def toString = elem.toString

    def c(s: Symbol) = SymbolTag(s.name :: classes, id, name)

    def id(s: Symbol) = SymbolTag(classes, Some(s.name), name)
  }

  implicit def toSymbolTag(s: Symbol) = SymbolTag(Nil, None, s.name)
}
