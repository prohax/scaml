package org.prohax.scaml

import org.specs._
import example.views.output.SongView
import example.models.Song

class RenderingSpec extends Specification {
  val List(track1,track2,track3) = List(
    Song("Starlings", "Elbow", 320, 167),
    Song("Mother Greer", "Augie March", 270, 160),
    Song("All You Need Is Love", "The Beatles Lossless", 231, 883)
  )

  "The SongView" should {
    "render a song" in {
      SongView.show(track1).toString must ==(
    <html>
      <head>
        <title>
          Starlings
          by
          Elbow
        </title>
      </head>
      <body>
        <p class="title">Starlings</p><p class="artist">Elbow</p><p class="duration">
      320
      seconds
    </p><p class="bitrate">
      167
      kbps
    </p>
      </body>
    </html>.toString)
    }

    "list the songs" in {
      SongView.list(List(track1,track2,track3)).toString must ==(
    <html>
      <head>
        <title>All songs</title>
      </head>
      <body>
        <div class="first"><p class="title">Starlings</p><p class="artist">Elbow</p><p class="duration">
      320
      seconds
    </p><p class="bitrate">
      167
      kbps
    </p></div><div class="rest">
              <p>
                  <span class="title">Mother Greer</span>
                  <span class="artist">Augie March</span>
                </p><p>
                  <span class="title">All You Need Is Love</span>
                  <span class="artist">The Beatles Lossless</span>
                </p>
            </div>
      </body>
    </html>.toString)
    }

    "list no songs" in {
      SongView.list(Nil).toString must ==(
    <html>
      <head>
        <title>All songs</title>
      </head>
      <body>
        <p class="alert">No songs found!</p>
      </body>
    </html>.toString)
    }
  }
}