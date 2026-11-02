lazy val root = project.in(file(".")).aggregate(a)
lazy val a = project
scalaVersion := "2.13.16"
libraryDependencies += "org.specs2" %% "specs2-junit" % "4.8.3" % Test
