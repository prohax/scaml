package org.prohax

import Predef.{stringWrapper => _,_}
import java.lang.String
import collection.mutable.ArrayBuffer

case class Post(username: String, title: String, body: String)

object Main {
  def main(args: Array[String]) {
    val post = Post("glen", "opinions", "wack.")
    println(render3(post))
  }

  def render2(post: Post): NewScaml.SymbolTag = {
    import NewScaml._
    
    'head c 'winpants id 'head
  }

  def render3(post: Post) = {
    import Scaml._

    implicit val tags = new ArrayBuffer[Tag]

    val view = "html" {
      "head" {
        "title" {
          "Welcome to Win Towne."
        }
      }
      "body" {
        "h1" {
          "Hello, " + post.username
        }
      }
    }

    println(tags)

    view

  }
//      "head"
//      "body.class1.class2" {
//        "ul#posts" {
//          posts.map { post =>
//            "li.post" {
//              post.body
//            }
//          }
//        }
//        "a.blah"('href -> "lol") {
//          "link text"
//        }
//      }
//    }
//  }

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