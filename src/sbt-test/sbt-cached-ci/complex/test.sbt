lazy val root = project.in(file(".")).aggregate(a, b)
lazy val a = project
lazy val b = project.settings(
  cachedCiTestQuick := cachedCiTestQuick.dependsOn(a / cachedCiTestQuick).value,
  cachedCiTestFull := cachedCiTestFull.dependsOn(a / cachedCiTestFull).value,
)
ThisBuild / scalaVersion := "2.13.1"
ThisBuild / crossScalaVersions := Seq("2.12.10", scalaVersion.value)
ThisBuild / libraryDependencies += "org.specs2" %% "specs2-junit" % "4.8.3" % Test
