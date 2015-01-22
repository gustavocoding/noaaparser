package noaaParser

object Usage extends App {

  val dataDir = "/home/gfernandes/Downloads/weather"

  // Getting files from specific years
  new Downloader(fromYear = 2013, toYear = 2015, destFolder = dataDir).download()

  // Extracting
  new Extractor(dataDir).extractAll()

  // Extract year from filenames
  def extractYear(filename: String) = filename.substring(filename.size - 7, filename.size - 3).toInt

  // Load daily summaries, filtering by country and/or year
  val opLoader = new OpLoader(dataDir)(f => f.getName.endsWith("op") && extractYear(f.getName) > 2010)
  val summaries = opLoader.getSummaries(d => d.station.exists(_.country.exists(_.code == "UK")))
  println(s"Loaded ${summaries.size} summaries.")
}
