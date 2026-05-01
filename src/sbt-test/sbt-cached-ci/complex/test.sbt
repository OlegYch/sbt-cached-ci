lazy val root = project.in(file(".")).aggregate(a, b)
lazy val a = project
lazy val b = project.settings(
  cachedCiTestQuick := cachedCiTestQuick.dependsOn(a / cachedCiTestQuick).value,
  cachedCiTestFull := cachedCiTestFull.dependsOn(a / cachedCiTestFull).value,
)
scalaVersion := "2.13.16"
crossScalaVersions := Seq("2.12.20", scalaVersion.value)
libraryDependencies += "org.specs2" %% "specs2-junit" % "4.8.3" % Test
