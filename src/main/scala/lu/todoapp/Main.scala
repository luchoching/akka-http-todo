import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.marshallers.xml.ScalaXmlSupport._
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.stream.ActorMaterializer
import spray.json.DefaultJsonProtocol

final case class Item(name: String, id: Long)

// collect our json format instances into a support trait
trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val itemFormat = jsonFormat2(Item)
}

object Main extends App with JsonSupport {
  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()
  implicit val ec = system.dispatcher

  val route =
    pathSingleSlash {
      println("hi!!")
      complete("Hello World!")
    } ~
    path("foo") {
      complete("/foo")
    } ~
    path("foo" / "bar") {
      complete("/foo/bar")
    } ~
    pathPrefix("ball") {
      pathEnd {
        complete("/ball")
      } ~
      path(IntNumber) { int =>
        complete(if (int % 2 == 0) "even ball" else "odd ball")
        }
    }

  val route2 =
    get {
      complete("receive GET")
    } ~
    complete("Receive Others!")

  val route3 =
    get {
      pathSingleSlash {
        complete {
          Item("thing", 42)
        }
      }
    }


  val bindingFuture = Http().bindAndHandle(route3,"localhost", 5678)
  println("""Server online at http://localhost:5678""")

}