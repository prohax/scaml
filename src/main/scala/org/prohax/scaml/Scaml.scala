package org.prohax.scaml

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

  case class TagFromString(n: String, buffer: ArrayBuffer[Tag], as: Map[Symbol, String]) {
    val state = new ArrayBuffer[Tag]
    state appendAll buffer
    buffer.clear

    def apply(attrs: (Symbol, String)*) = TagFromString(n, buffer, Map(attrs: _*))

    def apply(u: Unit) = {
      createSelf()
      ()
    }

    def apply(s: String) = {
      buffer append StringTag(s)
      createSelf()
      ()
    }

    def partition(str:String, r: String): (String, Option[String]) = {
      val i = str.indexOf(r)
      ("lol", None)
    }

    private def createSelf() {
      val tokens = n.split("[\\.#]").toList
      val separators = n.split("[^\\.#]*").toList.filter(_ != "")
      val as2 = Map(separators.map(_ match {
          case "." => 'class
          case "#" => 'id
      }).zip(tokens.drop(1)) : _*)
      state append new Tag {
        val attrs = as ++ as2
        val tags = new ArrayBuffer[Tag]
        tags appendAll buffer
        buffer.clear
        val name = tokens.first
      }
      buffer appendAll state
    }
  }

  implicit def stringWithTags(s: String)(implicit ab: ArrayBuffer[Tag]) = TagFromString(s, ab, Map())

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
