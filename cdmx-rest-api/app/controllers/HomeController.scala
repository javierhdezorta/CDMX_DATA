package controllers

import dao.MetrobusDAO
import javax.inject._
import play.api.mvc._
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json, Reads}

import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import play.api.libs.ws._
import models.RequestId


@Singleton
class HomeController @Inject()(daoService: MetrobusDAO,val ws: WSClient,val cc: ControllerComponents,val ec: ExecutionContext) extends AbstractController(cc) {

  private val logger = play.api.Logger(this.getClass)
  implicit val reads: Reads[RequestId] = Json.reads[RequestId]

  def getAlcaldiasService: Action[AnyContent] = Action.async {
    implicit request: Request[AnyContent] =>
      daoService.getAlcaldias.map{
        alcaldias => Ok(Json.toJson(alcaldias))
      }
  }

  def getUnidadesService: Action[AnyContent] = Action.async {
    implicit request: Request[AnyContent] =>
      daoService.getUnidades.map{
        unidades => Ok(Json.toJson(unidades))
      }
  }

  def getHistorialService: Action[JsValue] = Action.async(parse.json) {
    implicit request => request.body.validate[RequestId]  match {
      case JsSuccess(request, _) =>
        val idRequest = request.id
        daoService.getHistorial(idRequest).map{
          historial => Ok(Json.toJson(historial))
        }
      case JsError(errors) => Future(InternalServerError)
    }
  }

  def getUnidadesEnAlcaldiasService: Action[AnyContent] = Action.async {
    implicit request: Request[AnyContent] =>
      daoService.getUnidadesEnAlcaldias.map{
        unidadesAlcaldias => Ok(Json.toJson(unidadesAlcaldias))
      }
  }

}
