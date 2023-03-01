package com.tudux.actors

import akka.actor.{Actor, ActorLogging, Props}
import requests.Response
import io.circe._
import io.circe.parser._

object WikipediaActor {

  case class TitlesResponse(title: String, arr1: Array[String], arr2: Array[String], arr3: Array[String])

  sealed trait WikipediaCommand
  case class GetRelatedTitles(title: String) extends WikipediaCommand
  case class GetTextInfo(exactTitle: String) extends WikipediaCommand

  sealed trait WikipediaResponse
  case class GetTitlesResponse(titles: List[String])

  def props(titleEndpoint: String): Props = Props(new WikipediaActor(titleEndpoint))
}
class WikipediaActor(titleEndpoint: String) extends Actor with ActorLogging {

  import WikipediaActor._

  val endpoint = "https://en.wikipedia.org/w/api.php?action=opensearch&search="

  override def receive: Receive = {

    case GetRelatedTitles(title) =>
      val cleanedTitle = title.replace(" ", "$")
      val r: Response = requests.get(titleEndpoint.concat(cleanedTitle))
      val parseResult: Either[ParsingFailure, Json] = parse(r.text)
      //log.info(s"Get the logging stuff ${parseResult.getOrElse("")[1]}")
      val titles = parseResult match {
        case Right(json) =>
          val titles = json.asArray.get(1)
            .asArray.get.map(x => x.asString.get).toList
          titles
        case Left(_) =>
          log.error(s"Failed parsing $title")
          List("")
      }
      sender() ! GetTitlesResponse(titles)

  }
}


