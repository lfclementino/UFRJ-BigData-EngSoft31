name := """play-scala"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.11"

libraryDependencies += jdbc
libraryDependencies += cache
libraryDependencies += ws
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.0" % Test

libraryDependencies ++= Seq(
   "com.fasterxml.jackson.core" % "jackson-core" % "2.8.7",
   "com.fasterxml.jackson.core" % "jackson-databind" % "2.8.7",
   "com.fasterxml.jackson.core" % "jackson-annotations" % "2.8.7",
   "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.8.7",
   "org.apache.spark" % "spark-core_2.11" % "2.0.0",
   "org.apache.spark" % "spark-sql_2.11" % "2.0.0"
)


