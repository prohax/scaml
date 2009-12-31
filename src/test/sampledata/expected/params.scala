def params(posts: List[(String, List[String], Date)], title: String) = {
    <div id='posts'>
      <h1>{ title }</h1>
      <h1>
        Got
        { posts.length }
        posts for ya.
      </h1>
      <div class='spacer'>
        <ul>
          { posts.map(p =>
            <li>
              <span class='author'>{ p._1 }</span>
              <span class='posts'>{ p._2.take(1) }</span>
              <span class='date'>{ "%tD" format p._3 }</span>
            </li>
          ) }
        </ul>
      </div>
    </div>
}