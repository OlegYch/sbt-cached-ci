val addDep = settingKey[Boolean]("")
lazy val root = project.in(file(".")).settings(
  scalaVersion := "2.13.16",
  addDep := false,
  libraryDependencies ++= (if (addDep.value) Seq("com.softwaremill.macwire" %% "util" % "2.6.6") else Nil),
)
