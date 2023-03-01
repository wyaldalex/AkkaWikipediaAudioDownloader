package com.tudux.actors

import akka.actor.{Actor, ActorLogging, Props}
import com.tudux.wikipedia.WikipediaApi

object WikipediaArticleActor {

  sealed trait WikipediaArticleCommand
  case class GetTextInfo(title: String) extends WikipediaArticleCommand

  sealed trait WikipediaArticleResponse
  case class GetArticleResponse(text: Either[String,String]) extends WikipediaArticleResponse

  def props(api: WikipediaApi): Props = Props(new WikipediaArticleActor(api))
}
class WikipediaArticleActor(api: WikipediaApi) extends Actor with ActorLogging {

  import WikipediaArticleActor._

  override def receive: Receive = {
    case GetTextInfo(title) =>
      val articleText = api.getArticleText(title)
      sender() ! GetArticleResponse(articleText)
  }
}
