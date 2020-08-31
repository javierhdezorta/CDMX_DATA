package models
import play.api.libs.json._


case class Historial(date_updated:String,position_latitude:Double,position_longitude:Double)

object Historial {
  implicit val format: OFormat[Historial] = Json.format[Historial]
}