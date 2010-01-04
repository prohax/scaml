package org.prohax.scaml

import org.specs._
import example.views.output.SongView
import example.models.Song

class RenderingSpec extends Specification {
  "The SongView" should {
    "render a song or two" in {
      SongView.show(Song("Starlings", "Elbow", 320, 167)).toString must beEqualTo(
    <html>
      <head>
        <title>Song</title>
      </head>
      <body>
        <p class="title">Starlings</p>
        <p class="artist">Elbow</p>
        <p class="duration">
          320
          seconds
        </p>
        <p class="bitrate">
          167
          kbps
        </p>
      </body>
    </html>.toString)
    }
  }
}