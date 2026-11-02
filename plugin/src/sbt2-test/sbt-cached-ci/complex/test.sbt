lazy val root = project.in(file(".")).aggregate(a, b)
lazy val logTask = taskKey[Unit]("")
lazy val a = project.settings(
  logTask := streams.value.log.info("Running a/testQuick"),
  Test / testQuick := (Test / testQuick).dependsOn(logTask).evaluated,
)
lazy val b = project.settings(
  logTask := streams.value.log.info("Running b/testQuick"),
  Test / testQuick := (Test / testQuick).dependsOn(logTask).evaluated,
  cachedCiTestQuick := cachedCiTestQuick.dependsOn((a / Test / testQuick).toTask("")).value,
)
scalaVersion := "2.13.16"
crossScalaVersions := Seq("2.12.20", scalaVersion.value)
libraryDependencies += "org.specs2" %% "specs2-junit" % "4.8.3" % Test
