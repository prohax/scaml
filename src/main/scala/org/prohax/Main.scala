package org.prohax

import java.lang.String

case class Post(username: String, title: String, body: String)

object Main {
  def main(args: Array[String]) {
    val post = Post("glen", "opinions", "wack.")
    println(render2(post))
  }

  def render2(post: Post): NewScaml.SymbolTag = {
    import NewScaml._
    
    'head c 'winpants id 'head
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