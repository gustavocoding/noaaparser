package noaaParser

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.sys.process._

class Downloader(fromYear: Int, toYear: Int, destFolder: String) {

  def download() = {
    s"wget -nvc http://www1.ncdc.noaa.gov/pub/data/gsod/country-list.txt -P $destFolder".!
    s"wget -nvc  ftp://ftp.ncdc.noaa.gov/pub/data/noaa/isd-history.csv -P $destFolder".!
    val futures = (fromYear to toYear).map(y => Future {
      s"wget -nvc http://www1.ncdc.noaa.gov/pub/data/gsod/$y/gsod_$y.tar -P $destFolder" !
    })
    val seq = Future.sequence(futures)
    Await.result(seq, Duration.Inf)
  }

}