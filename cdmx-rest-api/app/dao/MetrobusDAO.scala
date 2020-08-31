package dao

import javax.inject.Inject
import anorm._
import anorm.SqlParser.{double, get, str}
import models.{Alcaldias, Historial, Unidades}
import play.api.db.DBApi
import scala.concurrent.Future
import scala.language.postfixOps

@javax.inject.Singleton
class MetrobusDAO @Inject()(dbapi: DBApi)(implicit ec: DatabaseExecutionContext) {

  private val db = dbapi.database("default")


  private[dao] val unidades = {
      str("vehicle_id") map (vehicle_id => Unidades(vehicle_id))
  }

  private[dao] val historial = {
    str("date_updated") ~
    double("position_latitude" ) ~
    double("position_longitude")  map {
      case date_updated ~ position_latitude ~ position_longitude => Historial(date_updated,position_latitude,position_longitude)}
  }

  private[dao] val alcaldias = {
    str("nomgeo") map (nomgeo => Alcaldias(nomgeo))
  }


  def getUnidades: Future[List[Unidades]] = Future(
    db.withConnection {
      implicit connection =>
        val results: List[Unidades] = SQL"select distinct(vehicle_id)  from  metrobus_cdmx".as(unidades *)
        results
    }
  )

  def getHistorial(id : Long): Future[List[Historial]] = Future(
    db.withConnection {
      implicit connection =>
        val query = s"select date_updated,position_latitude,position_longitude from metrobus_cdmx where vehicle_id = '$id' "
        val results: List[Historial] = SQL(query) .as(historial *)
        results
    }
  )

  def getAlcaldias: Future[List[Alcaldias]] = Future(
    db.withConnection {
      implicit connection =>
        val results: List[Alcaldias] = SQL"select distinct(nomgeo) from metrobus_cdmx where nomgeo IS NOT NULL".as(alcaldias *)
        results
    }
  )

  def getUnidadesEnAlcaldias: Future[List[Unidades]] = Future(
    db.withConnection {
      implicit connection =>
        val results: List[Unidades] = SQL"select distinct(vehicle_id) from metrobus_cdmx where nomgeo IS NOT NULL".as(unidades *)
        results
    }
  )

}