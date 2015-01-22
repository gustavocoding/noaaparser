package noaaParser

import scala.io.Source
import scala.util.Try

case class Country(code: String, name: String)

case class Station(usaf: String, wban: Int, name: String, country: Option[Country], latitude: Option[Float], longitude: Option[Float])

/**
 * Loader for countries and stations
 * @param dataDir location of the downloaded files ftp://ftp.ncdc.noaa.gov/pub/data/gsod/
 */
class InventoryLoader(dataDir: String) {
  val CountryFile = "country-list.txt"
  val StationFile = "isd-history.csv"
  lazy val Countries: Map[String, Country] = {
    val tuples = Source.fromFile(s"$dataDir/$CountryFile").getLines()
      .filter(!_.isEmpty)
      .filter(l => List(":", "FIPS").forall(v => !l.startsWith(v)))
      .map(_.split("          "))
      .map(a => a(0) -> Country(a(0), a(1)))
    Map(tuples.toList: _*)
  }


  lazy val Stations: Map[String, Station] = {
    val items = Source.fromFile(s"$dataDir/$StationFile").getLines().toStream.tail
      .map(_.split(","))
      .map(_.map(_.replaceAll( """"""", "")))
      .map(v => Station(v(0), v(1).toInt, v(2), Countries.get(v(3)), toFloat(v(5)), toFloat(v(6))))
      .map(s => s.usaf -> s)
    Map(items.toList: _*)
  }

  private def toFloat(s: String) = Try(s.toFloat).toOption

}