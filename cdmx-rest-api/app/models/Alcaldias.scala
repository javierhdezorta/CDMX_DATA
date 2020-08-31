package models
import play.api.libs.json._

case class Alcaldias(nomgeo:String)

object Alcaldias {
  implicit val format: OFormat[Alcaldias] = Json.format[Alcaldias]
}
