lazy val root = project.in(file("."))
  .settings(scalaVersion := "2.13.1", crossScalaVersions := List(scalaVersion.value))
  .aggregate(a).dependsOn(a)
lazy val a = project
  .settings(scalaVersion := "2.13.1", crossScalaVersions := List("2.12.11", scalaVersion.value))
