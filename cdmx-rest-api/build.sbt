name := """cdmx-rest-api"""
organization := "com.cdmx"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.3"

libraryDependencies += guice
libraryDependencies ++= Seq(
  ws,
  ehcache,
  jdbc,
  "org.postgresql" % "postgresql" % "42.2.5",
  "org.joda" % "joda-convert" % "1.9.2",
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test,
  "com.fasterxml.jackson.core" % "jackson-core" % "2.11.1",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.11.1",
  "com.fasterxml.jackson.core" % "jackson-annotations" % "2.11.1",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.11.2",
  "org.playframework.anorm" %% "anorm" % "2.6.7"
)

dependencyOverrides ++= Seq(
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.11.1"
)



// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"
