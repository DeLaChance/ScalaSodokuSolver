import sbt.Keys._

name := """ScalaSudokuSolver"""
version := "1.0"
scalaVersion := "2.13.1"
organization := "nl.lucien"
javacOptions ++= Seq("-source", "11", "-target", "11")

val playVersion = "2.8.1"
libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play" % playVersion,
  "com.typesafe.play" %% "play-json" % playVersion,
  "com.typesafe" % "config" % "1.4.1",

  jdbc,
  "org.postgresql" % "postgresql" % "42.1.1",
  "org.playframework.anorm" %% "anorm" % "2.6.5",

  "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % "test",
)
libraryDependencies += guice

fork in run := true

lazy val root = (project in file(".")).enablePlugins(PlayScala)