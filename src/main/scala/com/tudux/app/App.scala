package com.tudux.app

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.getFromResourceDirectory
import com.tudux.actors.WikipediaActor
import com.tudux.http.routes.MainRouter
import com.typesafe.config.ConfigFactory

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

object App extends App {

  def startHttpServer(wikipediaActor: ActorRef)(implicit system: ActorSystem): Unit = {

    implicit val scheduler: ExecutionContext = system.dispatcher

    val host = "0.0.0.0"
    val port = sys.env.getOrElse("PORT", "10001").toInt

    val router = new MainRouter(wikipediaActor)
    val routes = router.routes

    val bindingFuture = Http().newServerAt(host, port).bind(routes)

    bindingFuture.onComplete {
      case Success(binding) =>
        val address = binding.localAddress
        system.log.info(s"Server online at http:// ${address.getHostString}:${address.getPort}")

      case Failure(exception) =>
        system.log.error(s"Failed to bing HTTP server, because: $exception")
        system.terminate()
    }

  }

  val config = ConfigFactory.load("application.conf").getConfig("my-app")
  val wikiTitleEndpoint = config.getString("wiki-title-endpoint")
  val wikiArticleEndpoint = config.getString("wiki-article-endpoint")

  implicit val system: ActorSystem = ActorSystem("YellowTaxiCluster")
  val wikipediaActor = system.actorOf(WikipediaActor.props(wikiTitleEndpoint), "wikipediaActor")
  startHttpServer(wikipediaActor)
}
