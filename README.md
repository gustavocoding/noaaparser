# NOAA parser

## Usage (Scala)

```scala
 val dataDir = "/path/to/destination/"

// Getting files from specific years
new Downloader(fromYear = 2013, toYear = 2015, destFolder = dataDir).download()

// Extracting
new Extractor(dataDir).extractAll()

// Extract year from filenames
def extractYear(filename: String) = filename.substring(filename.size - 7, filename.size - 3).toInt

// Load daily summaries, filtering by country and/or year
val opLoader = new OpLoader(dataDir)(f => f.getName.endsWith("op") && extractYear(f.getName) == 2014)
val summaries = opLoader.getSummaries(d => d.station.exists(_.country.exists(_.code == "UK")))
println(s"Loaded ${summaries.size} summaries.")
```

## Usage (Java 8)

### Preparation

Publish the artifacts to the local maven repository:

```
./sbt +publish
```

then add the dependency to your pom:

```xml
<dependency>
    <groupId>noaaloader</groupId>
    <artifactId>noaaloader_2.11</artifactId>
    <version>1.0</version>
</dependency>
```

```java
String destFolder = "/path/to/destination/";
new Downloader(2010, 2010, destFolder).download();
new Extractor(destFolder).extractAll();
OpLoader opLoader = new OpLoader(destFolder, func(f -> f.getName().endsWith("op")));
ParArray<DaySummary> summaries = opLoader.getSummaries();
DaySummary first = summaries.apply(0);

Station station = first.station().get();
System.out.printf("Station: %s located in %s @(%f,%f)\n", station.name(), station.country().get().name(), station.latitude().get(), station.longitude().get());
System.out.printf("Measured on %d/%d/%d\n", first.day().d(),first.day().m(), first.day().y());
System.out.printf("Average temp: %fC\n", first.avgTemp().toCelsius());
System.out.printf("Minimum temp: %fC\n", first.minTemp().toCelsius());
System.out.printf("Maximum temp: %fC\n", first.maxTemp().toCelsius());
```
