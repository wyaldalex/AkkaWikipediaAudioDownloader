package com.tudux.actors

import akka.actor.{Actor, ActorLogging, Props}
import com.tudux.wikipedia.WikipediaApi

object WikipediaTitleActor {

  sealed trait WikipediaTitleCommand
  case class GetRelatedTitles(title: String) extends WikipediaTitleCommand

  sealed trait WikipediaTitleResponse
  case class GetTitlesResponse(titles: Either[String,List[String]]) extends WikipediaTitleResponse

  def props(api: WikipediaApi): Props = Props(new WikipediaTitleActor(api))
}
class WikipediaTitleActor(api: WikipediaApi) extends Actor with ActorLogging {

  import WikipediaTitleActor._

  override def receive: Receive = {

    case GetRelatedTitles(title) =>
      val titles = api.getRelatedArticleTitles(title)
      sender() ! GetTitlesResponse(titles)
  }
}


