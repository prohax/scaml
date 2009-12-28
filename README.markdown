# scaml

scaml is a Scala version/dialect/rip-off/etc of HAML. It is an attempt to bring the beauty and ease of markup in HAML to a significantly different language and enviorment.

## what's it look like?

Take this simple SCAML file:

	!!!
	%html
	  %head
	    %title
	  %body
	    %div#first
	      %h1.megaBig
	      %div#name.super.win
	        %p.thin
	          %strong
	        %p.wide
	        %br
	    %div#last

This is rendered to the following Scala source file:

	package org.prohax.scaml.output

	import scala.xml._
	import org.prohax.scaml.ScamlFile

	object classesandids extends ScamlFile {
	  override def headers = """<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">"""

	  def renderXml() = {
	    <html>
	      <head>
	        <title/>
	      </head>
	      <body>
	        <div id='first'>
	          <h1 class='megaBig'/>
	          <div id='name' class='super win'>
	            <p class='thin'>
	              <strong/>
	            </p>
	            <p class='wide'/>
	            <br/>
	          </div>
	        </div>
	        <div id='last'/>
	      </body>
	    </html>
	  }
	}

Then, when rendered, produces the following HTML:

	<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
	<html>
	  <head>
	    <title />
	  </head>
	  <body>
	    <div id="first">
	      <h1 class="megaBig" />
	      <div class="super win" id="name">
	        <p class="thin">
	          <strong />
	        </p>
	        <p class="wide" />
	        <br />
	      </div>
	    </div>
	    <div id="last"></div>
	  </body>
	</html>

Note that the above SCAML is valid HAML, and running it through the HAML compiler gives:

	<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
	<html>
	  <head>
	    <title></title>
	  </head>
	  <body>
	    <div id='first'>
	      <h1 class='megaBig'></h1>
	      <div class='super win' id='name'>
	        <p class='thin'>
	          <strong></strong>
	        </p>
	        <p class='wide'></p>
	        <br />
	      </div>
	    </div>
	    <div id='last'></div>
	  </body>
	</html>

## embedded Scala

SCAML allows embedding of code very similarly to HAML:

	%p
	  Counting to three:
	  %ul
	    = (1 to 3).map(i =>
	      %li= i
	%p
	  First 9 squares:
	  %ul
	    = (1 to 9).map { i =>
	      %li= i*i
	%p
	  Nesting times:
	  %table
	    = List((1,2), (3,4), (5,6)).map { x =>
	      %tr
	        %td= x._1
	        = (1 to x._2).map { i =>
	          %td= i

This produces nice, clean Scala code.

	def renderXml() = {
	  <p>
	    Counting to three:
	    <ul>
	      { (1 to 3).map(i =>
	        <li>{ i }</li>
	      ) }
	    </ul>
	  </p>
	  <p>
	    First 9 squares:
	    <ul>
	      { (1 to 9).map { i =>
	        <li>{ i*i }</li>
	      } }
	    </ul>
	  </p>
	  <p>
	    Nesting times:
	    <table>
	      { List((1,2), (3,4), (5,6)).map { x =>
	        <tr>
	          <td>{ x._1 }</td>
	          { (1 to x._2).map { i =>
	            <td>{ i }</td>
	          } }
	        </tr>
	      } }
	    </table>
	  </p>
	}

And the following, slightly less clean HTML.

	<p>
	  Counting to three:
	  <ul>
	    <li>1</li><li>2</li><li>3</li>
	  </ul>
	</p><p>
	  First 9 squares:
	  <ul>
	    <li>1</li><li>4</li><li>9</li><li>16</li><li>25</li><li>36</li><li>49</li><li>64</li><li>81</li>
	  </ul>
	</p><p>
	  Nesting times:
	  <table>
	    <tr>
	        <td>1</td>
	        <td>1</td><td>2</td>
	      </tr><tr>
	        <td>3</td>
	        <td>1</td><td>2</td><td>3</td><td>4</td>
	      </tr><tr>
	        <td>5</td>
	        <td>1</td><td>2</td><td>3</td><td>4</td><td>5</td><td>6</td>
	      </tr>
	  </table>
	</p>

## so what?

Well, the idea is to flesh this out until it supports as much of the HAML spec as possible, and provides a legitimate option for JVM web apps. I'd like to integrate it with [Play](http://www.playframework.org/), which I see as the most promising web framework in the world right now, for the JVM or otherwise.

## next steps

Passing variables into these templates is more difficult than in HAML, since the Scala source must be compiled. I'm planning on adopting something similar to [SXT](http://github.com/nkpart/sxt).

Figuring a nice way to work with the Scala branch of Play would be nice too.

And deploying as a [javagem](https://www.javagems.org/) would be kickin rad.

## problems

- Using the built-in Scala XHTML output gives little control over the resulting HTML, unlike HAML.
- Needs to be integrated into frameworks before anyone could start using it, but that's ok because
- Needs to support another ton of HAML before anyone could start using it.