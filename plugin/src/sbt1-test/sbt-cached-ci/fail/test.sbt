lazy val root = project.in(file(".")).settings(
  scalaVersion := "2.12.21",
  cachedCiTestFull := Def.task {sys.error("failing test")}.value,
  cachedCiTestFullToken := "",
)
