package org.prohax

import collection.mutable.{HashMap, Map}
import java.lang.String

case class Post(username: String, title: String, body: String)

object Main {
  def main(args: Array[String]) {
    println(render(Post("glen", "opinions", "wack.")))
  }

  def render(post: Post) = {
    import Samlv1._
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