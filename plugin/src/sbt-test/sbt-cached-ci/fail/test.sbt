lazy val root = project.in(file(".")).settings(
  scalaVersion := "2.12.20",
  cachedCiTestFull := Def.task {sys.error("failing test")}.value,
  cachedCiTestFullToken := "",
)
