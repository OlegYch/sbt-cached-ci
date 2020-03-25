lazy val root = project.in(file(".")).aggregate(a, b)
lazy val a = project
lazy val b = project.settings(cachedCiTest := cachedCiTest.dependsOn(a / cachedCiTest).value)
ThisBuild / scalaVersion := "2.13.1"
ThisBuild / libraryDependencies += "org.specs2" %% "specs2-junit" % "4.8.3" % Test
