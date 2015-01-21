object Usage extends App {

   val dataDir = "/home/gfernandes/temp"

   // Getting files from specific years
   new Downloader(fromYear = 2013, toYear = 2015, destFolder = dataDir).download()

   // Extracting
   new Extractor(dataDir).extractAll()

   // Extract year from filenames
   def extractYear(filename: String) = filename.substring(filename.size - 7, filename.size - 3).toInt

   // Load daily summaries
   val opLoader = new OpLoader(dataDir)(f => f.getName.endsWith("op") && extractYear(f.getName) > 2000)
   val summaries = opLoader.getSummaries
   println(s"Loaded ${summaries.size} summaries.")
}
