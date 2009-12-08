package org.prohax

import collection.mutable.{HashMap, Map}
import java.lang.String

object Tags {
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

  implicit def stringToTag(s: String) = new span(Map()) {
    override def toString = s
  }

  implicit def pairToMap(p: (Symbol, String)) = Map(p)

  case class div() extends Tag(Map())
  case class html(ts: Tag*) extends Tag(Map(), ts: _*)
  case class head(ts: Tag*) extends Tag(Map(), ts: _*)
  case class body(ts: Tag*) extends Tag(Map(), ts: _*)
  case class title(s: String) extends Tag(Map(), s)
  case class h1(ts: Tag*) extends Tag(Map(), ts: _*)
  case class h2(attr: (Symbol, String), ts: Tag*) extends Tag(Map(attr), ts: _*)
  case class p(attr: Map[Symbol, String], ts: Tag*) extends Tag(attr, ts: _*)
  object p {
    def apply(ts: Tag*): p = apply(Map[Symbol, String](), ts: _*)
  }
  case class span(attr: Map[Symbol, String], ts: span*) extends Tag(attr, ts: _*)
  object span {
    def apply(ts: span*): span = apply(Map[Symbol, String](), ts: _*)

    def apply(a1: (Symbol, String), a2: (Symbol, String), ts: span*): span = apply(Map(a1, a2), ts: _*)

    def apply(a1: (Symbol, String), a2: (Symbol, String), a3: (Symbol, String), ts: span*): span = apply(Map(a1, a2, a3), ts: _*)
  }
}

case class Post(username: String, title: String, body: String)

object Main {
  import Tags._
  def main(args: Array[String]) {
    println(render(Post("glen", "opinions", "wack.")))
  }

  def render(post: Post) = {
    html(
      head(
        title(post.title)
        ),
      body(
        h1(post.title, p("foo")),
        h2('class -> "us'er", post.username),
        p('class -> "post", post.body),
        span("span1"),
        span('class -> "span2", "span2"),
        span('class -> "span3", 'id -> "win", "span3"),
        span('class -> "span3", 'id -> "winboat", 'style -> "lol: strong", "span3")
        )
      )
  }
}