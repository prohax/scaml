package org.prohax.scaml.output

import scala.xml._
import org.prohax.scaml.ScamlFile

import org.prohax.scaml.models._

object params extends ScamlFile[(List[Post])] {
  def renderXml(t:(List[Post])) = t match { case (posts) =>
    <div id='posts'>
      <h1>
        Got
        { posts.length }
        posts for ya.
      </h1>
      <div class='spacer'>
        <ul>
          { posts.map(p =>
            <li>
              <span class='author'>{ p.author }</span>
              <span class='posts'>{ p.body.take(1) }</span>
              <span class='date'>{ "%tD" format p.date }</span>
            </li>
          ) }
        </ul>
      </div>
    </div>
  }
}