package example.views.output

import org.prohax.scaml.Helpers._
import scala.xml._

import example.models._

object SongView {
  def show(s: Song) = {
    <html>
      <head>
        <title>Song</title>
      </head>
      <body>
        <p class='title'>{s.title}</p>
        <p class='artist'>{s.artist}</p>
        <p class='duration'>
          {s.duration}
          seconds
        </p>
        <p class='bitrate'>
          {s.bitrate}
          kbps
        </p>
      </body>
    </html>
  }
}