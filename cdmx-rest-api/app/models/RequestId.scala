package models
import play.api.libs.json._


case class RequestId(id:Long)

object RequestId {
  implicit val format: OFormat[RequestId] = Json.format[RequestId]
}
