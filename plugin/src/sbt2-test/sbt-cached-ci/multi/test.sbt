ThisBuild / scalaVersion := "3.8.4"
lazy val root = project.in(file("."))
  .settings(
    Compile / compileIncremental ~= { r =>
      Thread.sleep(scala.util.Random.nextInt(1000))
      assert(!file("compiled").exists(), "Unexpected compilation")
      IO.touch(file("compiled"))
      r
    },
  )
lazy val a = project.in(file("a")).dependsOn(root)
lazy val b = project.in(file("b")).dependsOn(root)