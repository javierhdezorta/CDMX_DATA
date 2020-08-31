package models
import play.api.libs.json._


case class Unidades ( vehicle_id:String)

object Unidades {
  implicit val format: OFormat[Unidades] = Json.format[Unidades]
}