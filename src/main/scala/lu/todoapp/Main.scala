import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.marshallers.xml.ScalaXmlSupport._
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.stream.ActorMaterializer
import spray.json.{DefaultJsonProtocol, DeserializationException, JsNumber, JsObject, JsString, JsValue, RootJsonFormat}

final case class Item(name: String, id: Long)
final case class Order(items: List[Item])
final case class Color(val name: String, val red: Int, val green: Int, val blue: Int)


// collect our json format instances into a support trait
trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val itemFormat = jsonFormat2(Item)
  implicit val orderFormat = jsonFormat1(Order)

  implicit object ColorJsonFormat extends RootJsonFormat[Color] {
    def write(c: Color) = JsObject(
      "name" -> JsString(c.name),
      "red" -> JsNumber(c.red),
      "green" -> JsNumber(c.green),
      "blue" -> JsNumber(c.blue)
    )

    def read(value: JsValue) = {
      value.asJsObject.getFields("2", "red", "green", "blue") match {
        case Seq(JsString(name), JsNumber(red), JsNumber(green), JsNumber(blue)) =>
          Color(name, red.toInt, green.toInt, blue.toInt)
        case _ => throw new DeserializationException("Color expected")
      }
    }
  }
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
    } ~
    post {
      entity(as[Order]) { order =>
        val itemsCount = order.items.size
        val itemNames = order.items.map(_.name).mkString(", ")
        complete(s"Ordered $itemsCount items: $itemNames")

      }
    }

    val route4 =
      pathSingleSlash {
        post {
          entity(as[Color]) { color =>
            val colorName = color.name
            complete(s"Color name: $colorName")
          }
        }
      }


  val bindingFuture = Http().bindAndHandle(route4,"localhost", 5678)
  println("""Server online at http://localhost:5678""")

}