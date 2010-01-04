package example.views.output

import org.prohax.scaml.Helpers._
import scala.xml._

import example.models._

object SongView {
  def show(s: Song) = {
    <html>
      <head>
        <title>
          {s.title}
          by
          {s.artist}
        </title>
      </head>
      <body>
        {_show(s)}
      </body>
    </html>
  }

  def _show(s: Song) = {
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
  }

  def list(ss: List[Song]) = {
    <html>
      <head>
        <title>All songs</title>
      </head>
      <body>
        {
          if (ss.isEmpty) {
            <p class='alert'>No songs found!</p>
          } else {
            <div class='first'>{_show(ss.first)}</div>
            <div class='rest'>
              { ss.drop(1).map(s =>
                <p>
                  <span class='title'>{s.title}</span>
                  <span class='artist'>{s.artist}</span>
                </p>
              )}
            </div>
          }
        }
      </body>
    </html>
  }
}