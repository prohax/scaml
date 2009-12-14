package org.prohax

import Predef.{stringWrapper => _, _}
import java.lang.String
import collection.mutable.ArrayBuffer

case class Post(username: String, title: String, body: String)

object Main {
  def main(args: Array[String]) {
    val post = Post("Mr Some Lolfase", "I Haz A Opinion", "And it's lolled.")
    val post2 = Post("glen", "Why I Win", "QED")
    println(render3(post, post2))
  }

  def render2(post: Post): NewScaml.SymbolTag = {
    import NewScaml._

    'head c 'winpants id 'head
  }

  def render3(posts: Post*) = {
    import Scaml._

    implicit val tags = new ArrayBuffer[Tag]

    "html" {
      "head" {
        "title" {
          "Welcome to Win Towne."
        }
      }
      "body" {
        "h1.header"('href -> "http://lol.fase") {
          "Hello, " + posts.first.username
        }
        "div.spacer#spacerMan" {}
        "ol" {
          posts.foreach {post =>
            "li" {
              "h2.title" {post.title}
              "h6.username" {post.username}
              "p.body" {post.body}
            }
          }
        }
      }
    }

    tags.first

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