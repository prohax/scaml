package org.prohax

import collection.mutable.{HashMap, Map}
import java.lang.String

object Tags {
  case class Tag(tags: Any*) {
    val name = getClass.getSimpleName

    override def toString = if (tags.isEmpty) {
      "<" + name + "></" + name + ">"
    } else {
      "<" + name + ">\n" + tags.map(_.toString).mkString("\n") + "\n</" + name + ">"
    }
  }

  case class div() extends Tag()
  case class html(ts: Tag*) extends Tag(ts: _*)
  case class head(ts: Tag*) extends Tag(ts: _*)
  case class body(ts: Tag*) extends Tag(ts: _*)
  case class title(s: String) extends Tag(s)
  case class h1(ts: Tag*) extends Tag(ts: _*)
  case class h2(attr: (Symbol, String), ts: Tag*) extends Tag(ts: _*)
  case class p(attr: (Symbol, String), ts: Tag*) extends Tag(ts: _*)
  object p {
    def apply(ts: Tag*): p = apply('n -> "", ts: _*)
  }

  implicit def stringToTag(s: String) = new Tag {
    override def toString = s
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
        h2('class -> "user", post.username),
        p('class -> "post", post.body)
        )
      )
  }
}