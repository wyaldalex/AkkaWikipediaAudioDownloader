package com.tudux.wikipedia

import requests.Response
import io.circe._
import io.circe.parser._

sealed trait WikipediaApi {
  def getRelatedArticleTitles(title: String): Either[String,List[String]]
  def getArticleText(title: String): Either[String,String]
}

case class WikipediaCallsLihaoyi(endpoint: String) extends WikipediaApi {

  override def getArticleText(title: String): Either[String,String] = {
    val articleEndpoint = endpoint.concat("?action=query&format=json&titles=")
    val textExtractOptions = "&prop=extracts&explaintext"
    val cleanedTitle = title.replace(" ", "%20")
    val r: Response = requests.get(articleEndpoint.concat(cleanedTitle).concat(textExtractOptions))
    val parseResult: Either[ParsingFailure, Json] = parse(r.text)

    val titles = parseResult match {
      case Right(json) =>
        val titles = json
        Right(titles.toString())
      case Left(_) =>
        Left(s"Error retrieving $title from $articleEndpoint")
    }
    titles
  }

  override def getRelatedArticleTitles(title: String): Either[String,List[String]] = {
    val titlesEndpoint = endpoint.concat("?action=opensearch&search=")
    val cleanedTitle = title.replace(" ", "$")
    val r: Response = requests.get(titlesEndpoint.concat(cleanedTitle))
    val parseResult: Either[ParsingFailure, Json] = parse(r.text)

    val titles = parseResult match {
      case Right(json) =>
        val titles = json.asArray.get(1)
          .asArray.get.map(x => x.asString.get).toList
        Right(titles)
      case Left(_) =>
        Left(s"Error retrieving $title from $titlesEndpoint")
    }
    titles
  }
}
