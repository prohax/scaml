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

  implicit def stringToTag(s: String) = new Tag(Map()) {
    override def toString = s
  }

  case class div() extends Tag(Map())
  case class html(ts: Tag*) extends Tag(Map(), ts: _*)
  case class head(ts: Tag*) extends Tag(Map(), ts: _*)
  case class body(ts: Tag*) extends Tag(Map(), ts: _*)
  case class title(s: String) extends Tag(Map(), s)
  case class h1(ts: Tag*) extends Tag(Map(), ts: _*)
  case class h2(attr: (Symbol, String), ts: Tag*) extends Tag(Map(attr), ts: _*)
  case class p(attr: (Symbol, String), ts: Tag*) extends Tag(Map(attr), ts: _*)
  object p {
    def apply(ts: Tag*): p = apply('n -> "", ts: _*)
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
        p('class -> "post", post.body)
        )
      )
  }
}