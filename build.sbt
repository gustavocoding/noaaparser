name := "NOAALoader"

version := "1.0"

crossScalaVersions := Seq("2.10.4", "2.11.5")

publishTo := Some(Resolver.file("file",  new File(Path.userHome.absolutePath+"/.m2/repository")))

libraryDependencies += "org.scala-lang.modules" %% "scala-java8-compat" % "0.2.0"
    
