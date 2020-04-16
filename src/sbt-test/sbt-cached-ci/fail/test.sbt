lazy val root = project.in(file(".")).settings(cachedCiTestFull := Def.task {sys.error("failing test")}.value)
