package org.prohax.scaml


object Util {
  private def pairedV(p: (String, String)) = "%s=\"%s\"" format (p._1, p._2)

  private def handleBrackets(content: String, key: String, value: String): String = {
    val parser = new Parser {
      def xxx: Parser[String] = (".*%s=" format key).r ~ attributes ~ (".*".r) ^^ { case (pre ~ c ~ post) => {
        pre + ("{" + "Option(" + c + ") getOrElse \"%s\"}" format value) + post
      }}
    }
    parser.parse(parser.xxx, content).get
  }

  def addIfMissing(attributeContent: String, newAttributes: List[(String, String)]): String = {
    newAttributes.foldLeft(attributeContent)((b, pair) => {
      val (key, value) = pair
      b match { // Note: Order is important on cases.
        case _ if b.contains("%s=\"" format key) => b
        case _ if b.contains("%s={" format key) => handleBrackets(b, key, value)
        case _ if b.isEmpty => pairedV(pair)
        case _ => b + " " + pairedV(pair)
      }
    })
  }
}