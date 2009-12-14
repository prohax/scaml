package org.prohax

object Scaml {
  case class Tag(attrs: Map[Symbol, String], tags: Tag*) {
    val name = getClass.getSimpleName

    override def toString = {
      val tagOpen = "<" + name + attrs.map(kv => " " + kv._1.name + "='" + kv._2.replaceAll("'", "\\\\'") + "'").mkString + ">"
      if (tags.isEmpty) {
        tagOpen + "</" + name + ">"
      } else {
        "\n" + tagOpen + tags.map(_.toString).mkString + "</" + name + ">\n"
      }
    }
  }

  trait TagV2 {
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

  implicit def stringToTag(s: String) = new Tag(Map()) {
    override def toString = s
  }

  case class TagBuilder(n: String) {
    def apply(as: Map[Symbol, String], ts: Seq[Tag]) = new TagV2 {
      val attrs: Map[Symbol, String] = as
      val name = n
      val tags: Seq[Tag] = ts
    }

    def apply(as: Map[Symbol, String], ts: Tag*): TagV2 = apply(as, ts)

    def apply(): TagV2 = apply(Map[Symbol, String]())

    def apply(ts: Tag*): TagV2 = apply(Map[Symbol, String](), ts)

    def apply(a1: (Symbol, String), ts: Tag*): TagV2 = apply(Map(a1), ts)

    def apply(a1: (Symbol, String), a2: (Symbol, String), ts: Tag*): TagV2 = apply(Map(a1, a2), ts)
    def apply(a1: (Symbol, String), a2: (Symbol, String), a3: (Symbol, String), ts: Tag*): TagV2 = apply(Map(a1, a2, a3), ts)
  }

  val List(html, head, title, body, h1, h2, p, span, div) = List(
    "html", "head", "title", "body", "h1", "h2", "p", "span", "div"
    ).map(TagBuilder(_))

  implicit def toOldTag(t: TagV2): Tag = new Tag(t.attrs, t.tags: _ *) {
    override val name = t.name
  }
}

object NewScaml {
  import scala.xml.{TopScope, Elem, UnprefixedAttribute, Text, Null}

  case class SymbolTag(classes: List[String], id: Option[String], name: String) {
    val elem = Elem(null, name,
      new UnprefixedAttribute("class", classes.map(Text(_))),
        id.map(new UnprefixedAttribute("id", Text(_))) getOrElse Null, TopScope)

    override def toString = elem.toString

    def c(s: Symbol) = SymbolTag(s.name :: classes, id, name)
    def id(s: Symbol) = SymbolTag(classes, Some(s.name), name)
  }

  implicit def toSymbolTag(s: Symbol) = SymbolTag(Nil, None, s.name)
}
