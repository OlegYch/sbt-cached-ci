lazy val root = project.in(file("."))
  .settings(
    scalaVersion := "3.8.4",
    Compile / compileIncremental ~= { r =>
      IO.touch(file("compiled"))
      r
    },
    cachedCiTestFull := {
      assert(!file("compiled").exists(), "Unexpected compilation")
    },
  )
