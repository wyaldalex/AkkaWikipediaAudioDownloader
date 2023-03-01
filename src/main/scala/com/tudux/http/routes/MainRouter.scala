package com.tudux.http.routes

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationInt

class MainRouter(wikipediaActor: ActorRef)(implicit system: ActorSystem)
{
  
  implicit val dispatcher: ExecutionContext = system.dispatcher
  implicit val timeout: Timeout = Timeout(30.seconds)

  val textExtractorRoutes = TextExtractorRoutes(wikipediaActor)
  val pingRoutes = Ping()

  val routes: Route = {
    textExtractorRoutes.routes ~
      pingRoutes.routes
  }
}
