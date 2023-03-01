package com.tudux.http.routes.format

import com.tudux.actors.WikipediaArticleActor.GetArticleResponse
import com.tudux.actors.WikipediaTitleActor.GetTitlesResponse
import com.tudux.http.response.Responses.WikipediaResponse
import spray.json.DefaultJsonProtocol

object RouteFormats {

//  trait GetTitlesProtocol extends DefaultJsonProtocol {
//    implicit val getTitlesFormat = jsonFormat1(GetTitlesResponse)
//  }

  trait GetTitlesResponseProtocol extends DefaultJsonProtocol {
    implicit val getTitlesResponseFormat = jsonFormat1(GetTitlesResponse)
  }

  trait GetArticleResponseProtocol extends DefaultJsonProtocol {
    implicit val getArticleResponseFormat = jsonFormat1(GetArticleResponse)
  }

  trait GetWikipediaResponseProtocol extends DefaultJsonProtocol
    with GetTitlesResponseProtocol
    with GetArticleResponseProtocol  {
    implicit val getWikipediaResponseFormat = jsonFormat2(WikipediaResponse)
  }
}
