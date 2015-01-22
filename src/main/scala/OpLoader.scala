package noaaParser

import java.io.File
import java.util.concurrent.atomic.LongAdder

import scala.io.Source

case class YMD(y: Int, m: Int, d: Int) {
  override def toString: String = List(d, m, y).mkString(",")
}

object YMD {
  def apply(ymd: String): YMD = YMD(ymd.substring(0, 4).toInt, ymd.substring(4, 6).toInt, ymd.substring(6, 8).toInt)
}

case class TemperatureF(value: Float) {
  def toCelsius = (value - 32) * 5 / 9
}

object TemperatureF {
  def apply(s: String) = new TemperatureF(s.replaceAll("\\*", "").toFloat)
}

/**
 * The main entity representing global day weather summary
 * @param station Contains location information (Lat, Lon, Country, etc)
 * @param day D/M/Y of measurement
 * @param avgTemp avg Temp in F
 * @param minTemp min Temp in F
 * @param maxTemp max Temp in F
 */
case class DaySummary(station: Option[Station], day: YMD, avgTemp: TemperatureF, minTemp: TemperatureF, maxTemp: TemperatureF)

/**
 * Loader for op files
 * @param dataDir Location of the files downloaded from ftp://ftp.ncdc.noaa.gov/pub/data/gsod/
 * @param fileFilter Filter for files to load
 */
class OpLoader(dataDir: String)(fileFilter: File => Boolean) {

  val inventoryLoader = new InventoryLoader(dataDir)

  def loadSummary(opFile: String) = {
    Source.fromFile(opFile).getLines().toList
      .tail.map(_.split(" +")).map(a => {
      DaySummary(inventoryLoader.Stations.get(a(0)), YMD(a(2)), TemperatureF(a(3)), TemperatureF(a(18)), TemperatureF(a(17)))
    })
  }

  def getSummaries(summaryFilter: DaySummary => Boolean) = {
    val counter = new LongAdder
    val files: Array[File] = new File(dataDir).listFiles.filter(fileFilter)
    val summaries = files.par.flatMap(x => {
      counter.increment
      printf(s"\rLoading file ${counter.intValue()} of ${files.size} - ${x.getAbsolutePath}")
      loadSummary(x.getAbsolutePath).filter(summaryFilter)

    })
    println("\rLoading Done.")
    summaries
  }
}