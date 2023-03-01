package com.tudux.http.response

import com.tudux.actors.WikipediaArticleActor.GetArticleResponse
import com.tudux.actors.WikipediaTitleActor.GetTitlesResponse

object Responses {

  case class WikipediaResponse(articlesRelated: GetTitlesResponse, extract: GetArticleResponse)
}
