scalaVersion := "2.12.12"

name := "idiomatic"
organization := "ai.frw"
version := "1.0"

fork in console := true
trapExit := false

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"
libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.2"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.2" % "test"
libraryDependencies += "org.scalatest" %% "scalatest-funsuite" % "3.2.2" % "test"
libraryDependencies += "com.bot4s" %% "telegram-core" % "4.4.0-RC2"
libraryDependencies += "net.debasishg" %% "redisclient" % "3.30"
libraryDependencies += "commons-codec" % "commons-codec" % "1.15"
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.9.1"

enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)

mainClass in Compile := Some("ai.frw.idiomatic.Main")
packageName in Docker := "francescorw/idiomatic"