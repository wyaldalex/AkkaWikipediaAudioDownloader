package com.tudux.http.routes

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes.OK
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import akka.util.Timeout
import com.tudux.actors.WikipediaActor.{GetRelatedTitles, GetTitlesResponse}
import com.tudux.http.routes.format.RouteFormats.GetTitlesProtocol
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._
import spray.json._

import java.util.UUID
import scala.concurrent.{ExecutionContext, Future}

case class TextExtractorRoutes(wikipediaActor: ActorRef)(implicit system: ActorSystem, dispatcher: ExecutionContext, timeout: Timeout) extends SprayJsonSupport
 with GetTitlesProtocol {

  def toHttpEntity(payload: String): HttpEntity.Strict = HttpEntity(ContentTypes.`application/json`, payload)

  val routes: Route = {
    pathPrefix("api" / "wikipediatts") {
      get {
        parameters('title) { (title: String) =>
          complete(
            (wikipediaActor ? GetRelatedTitles(title))
              .mapTo[GetTitlesResponse]
              .map(_.toJson.prettyPrint)
              .map(toHttpEntity)
          )
        }
      }
    }
  }
}

