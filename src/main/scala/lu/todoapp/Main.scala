import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.marshallers.xml.ScalaXmlSupport._
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer

object Main extends App {
  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()
  implicit val ec = system.dispatcher

  val route =
    get {
      pathSingleSlash {
        complete {
          <html><body>Hello world!</body></html>
        }
      } ~
        path("ping") {
          complete("pong")
        } ~
        path("crash") {
          sys.error("BOOM!")
        }
    }

  val bindingFuture = Http().bindAndHandle(route,"localhost", 5678)
  println("""Server online at http://localhost:5678""")

}