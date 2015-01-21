package noaaParser

import java.io.File

import scala.sys.process._

class Extractor(dataDir: String) {

   private def extractTar(filePath: String) =
   {
      printf(s"\rExtracting $filePath")
      s"tar -C ${filePath.substring(0, filePath.lastIndexOf('/')) } -xvf $filePath" !!
   }

   private def gunzip = {
      s"find $dataDir -name *.gz -print0" #| "xargs -0 gunzip" !
   }

   def extractAll() = {
      val files: Array[String] = new File(dataDir).listFiles.map(_.getAbsolutePath).filter(_.endsWith(".tar"))
      files.flatMap(f => extractTar(f))
      gunzip

   }

}

