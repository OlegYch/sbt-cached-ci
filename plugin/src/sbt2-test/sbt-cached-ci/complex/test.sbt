lazy val root = project.in(file(".")).aggregate(a, b)
lazy val a = project
lazy val b = project
scalaVersion := "2.13.16"
crossScalaVersions := Seq("2.12.20", scalaVersion.value)
libraryDependencies += "org.specs2" %% "specs2-junit" % "4.8.3" % Test
