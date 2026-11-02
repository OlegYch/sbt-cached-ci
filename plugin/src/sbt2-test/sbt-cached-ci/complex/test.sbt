lazy val root = project.in(file(".")).aggregate(a, b)
lazy val a = project.settings(
  Test / testQuick := {
    streams.value.log.info("Running a/testQuick")
    val r = (Test / testQuick).evaluated
    r
  },
)
lazy val b = project.settings(
  Test / testQuick := {
    streams.value.log.info("Running b/testQuick")
    val r = (Test / testQuick).evaluated
    r
  },
  cachedCiTestQuick := cachedCiTestQuick.dependsOn((a / Test / testQuick).toTask("")).value,
)
scalaVersion := "2.13.16"
crossScalaVersions := Seq("2.12.20", scalaVersion.value)
libraryDependencies += "org.specs2" %% "specs2-junit" % "4.8.3" % Test
