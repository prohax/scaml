package org.prohax

import collection.mutable.{HashMap, Map}
import java.lang.String
import scala.xml.{TopScope, Elem}

case class Post(username: String, title: String, body: String)

object Main {
  def main(args: Array[String]) {
    val post = Post("glen", "opinions", "wack.")
    println(render2(post))
  }

  trait SymbolTag {
    val classes: Seq[String]
    val id: Option[String]
    val name: String

    def elem = Elem(null, name, null, TopScope)

    override def toString = elem.toString
  }

  implicit def toSymbolTag(s: Symbol) = new SymbolTag {
    val classes = Nil
    val id = None
    val name = s.name
  }

  def render2(post: Post): SymbolTag = {
    'head //c 'class id 'head
  }

  def render(post: Post) = {
    import Scaml._

    def user(ts: Tag*) = div('class -> "user", ts: _*)

    html(
      head(
        title(post.title)
        ),
      body(
        h1(post.title, p("foo")),
        h2('class -> "us'er", post.username),
        p('class -> "post", post.body),
        user("something"),
        span("span1"),
        span('class -> "span2", "span2"),
        span('class -> "span3", 'id -> "win", "span3"),
        span('class -> "span3", 'id -> "winboat", 'style -> "lol: strong", "span3")
        ))
  }
}