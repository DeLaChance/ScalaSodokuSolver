ThisBuild / scalaVersion := "2.12.7"
ThisBuild / organization := "nl.lucien"

lazy val hello = (project in file("."))
  .settings(
    name := "ScalaSodokuSolver",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % Test,
  )