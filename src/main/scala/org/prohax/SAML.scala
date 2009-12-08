package org.prohax

import collection.mutable.{HashMap, Map}

case class Tag
case class div extends Tag

object SAML {
  val tags = new HashMap[String, Tag]

  def register(name: String, f: () => Tag) {
    tags += (name -> f())
  }

  def render(name: String) {
    println(tags(name).getClass.getSimpleName)
  }
}

case class Request
case class User

object Application {
  def currentUser: Option[User] = None
}

class PostsController(request: Request) {
  val user = Application.currentUser
}


object Main {
  import Tag._
  def main(args: Array[String]) = {
    SAML.register("simple", () => {
      div()
    })

    SAML.render("simple")
  }
}