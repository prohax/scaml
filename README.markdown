# scaml

scaml is a Scala version/dialect/rip-off/etc of HAML. It is an attempt to bring the beauty and ease of markup in HAML to a significantly different language and enviorment.

## what's it look like?

Take this simple SCAML file:

	%div
	  %h1 Test.
	  %p
	    Welcome, Mr
	    %span.bold Boldfase

This is rendered to the following Scala source file:

	package org.prohax.scaml.output

	import scala.xml._
	import org.prohax.scaml.ScamlFile

	import example.models._

	object literals extends ScamlFile[Unit] {
	  def renderXml(t:Unit) = {
	    <div>
	      <h1>Test.</h1>
	      <p>
	        Welcome, Mr
	        <span class='bold'>Boldfase</span>
	      </p>
	    </div>
	  }
	}

Then, when rendered, produces the following HTML:

	<div>
	  <h1>Test.</h1>
	  <p>
	    Welcome, Mr
	    <span class="bold">Boldfase</span>
	  </p>
	</div>

Note that the above SCAML is valid HAML, and running it through the HAML compiler gives something virtually the same:

	<div>
	  <h1>Test.</h1>
	  <p>
	    Welcome, Mr
	    <span class='bold'>Boldfase</span>
	  </p>
	</div>

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

	def renderXml(t:Unit) = {
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

## passing variables

Usually, templates are passed (perhaps implicitly) model objects to render. Assuming we have a simple model and some instances:

	case class Post(author: String, body: List[String], date: Date)
	
	List(
	  Post("Glen", List("This is line 1", "and line 2", "and line 3"), new Date(2009, 12, 28)),
	  Post("Ben", List("Lol I am too sick for cider"), new Date(2009, 11, 28)),
	  Post("Dylan", List("I", "like", "cider."), new Date(2009, 10, 28))
	)

You write this SCAML:

	/! posts: List[Post]
	#posts
	  %h1
	    Got
	    = posts.length
	    posts for ya.
	  .spacer
	    %ul
	      = posts.map(p =>
	        %li
	          %span.author= p.author
	          %span.posts= p.body.take(1)
	          %span.date= "%tD" format p.date

Which renders this Scala source:

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

And this html:

	<div id="posts">
	  <h1>
	    Got
	    3
	    posts for ya.
	  </h1>
	  <div class="spacer">
	    <ul>
	      <li>
	          <span class="author">Glen</span>
	          <span class="posts">This is line 1</span>
	          <span class="date">01/28/10</span>
	        </li><li>
	          <span class="author">Ben</span>
	          <span class="posts">Lol I am too sick for cider</span>
	          <span class="date">12/28/09</span>
	        </li><li>
	          <span class="author">Dylan</span>
	          <span class="posts">I</span>
	          <span class="date">11/28/09</span>
	        </li>
	    </ul>
	  </div>
	</div>

## so what?

Well, the idea is to flesh this out until it supports as much of the HAML spec as possible, and provides a legitimate option for JVM web apps. I'd like to integrate it with [Play](http://www.playframework.org/), which I see as the most promising web framework in the world right now, for the JVM or otherwise.

## next steps

Passing variables into these templates is more difficult than in HAML, since the Scala source must be compiled. I've adopted something similar to [SXT](http://github.com/nkpart/sxt), but more constrained. It will take integration with a framework for this to be really locked down.

Figuring a nice way to work with the Scala branch of Play would be nice too.

And deploying as a [javagem](https://www.javagems.org/) would be kickin rad.

## problems

- Using the built-in Scala XHTML output gives little control over the resulting HTML, unlike HAML.
- Needs to be integrated into frameworks before anyone could start using it, but that's ok because
- Needs to support another ton of HAML before anyone could start using it.