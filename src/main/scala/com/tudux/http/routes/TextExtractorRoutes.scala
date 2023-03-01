package com.tudux.http.routes

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes.OK
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import akka.util.Timeout
import com.tudux.actors.WikipediaArticleActor.{GetArticleResponse, GetTextInfo}
import com.tudux.actors.WikipediaTitleActor.{GetRelatedTitles, GetTitlesResponse}
import com.tudux.http.response.Responses.WikipediaResponse
import com.tudux.http.routes.format.RouteFormats.{ GetWikipediaResponseProtocol}
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._
import spray.json._

import java.util.UUID
import scala.concurrent.{ExecutionContext, Future}

case class TextExtractorRoutes(titlesActor: ActorRef, articleActor: ActorRef)(implicit system: ActorSystem, dispatcher: ExecutionContext, timeout: Timeout) extends SprayJsonSupport
 with GetWikipediaResponseProtocol {

  def toHttpEntity(payload: String): HttpEntity.Strict = HttpEntity(ContentTypes.`application/json`, payload)

  def getWikipediaResponse(title: String) : Future[WikipediaResponse] = {

    val titlesFuture = (titlesActor ? GetRelatedTitles(title)).mapTo[GetTitlesResponse]
    val articleFuture = (articleActor ? GetTextInfo(title)).mapTo[GetArticleResponse]

    val wikiResponse = for {
      titles <- titlesFuture
      article <- articleFuture
    } yield WikipediaResponse(titles, article)
    wikiResponse
  }

  val routes: Route = {
    pathPrefix("api" / "wikipediatts") {
      get {
        parameters('title) { (title: String) =>
          onSuccess(getWikipediaResponse(title)) {
            case wikipediaResponse@WikipediaResponse(_, _) =>
              complete(HttpResponse(
                200,
                entity = HttpEntity(
                  ContentTypes.`application/json`,
                  wikipediaResponse.toJson.prettyPrint
                )))
          }
        }
      }
    }
  }
}

