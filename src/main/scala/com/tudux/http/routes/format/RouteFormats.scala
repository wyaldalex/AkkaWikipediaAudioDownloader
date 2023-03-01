package com.tudux.http.routes.format

import com.tudux.actors.WikipediaActor.GetTitlesResponse
import spray.json.DefaultJsonProtocol

object RouteFormats {

  trait GetTitlesProtocol extends DefaultJsonProtocol {
    implicit val getTitlesFormat = jsonFormat1(GetTitlesResponse)
  }
}
