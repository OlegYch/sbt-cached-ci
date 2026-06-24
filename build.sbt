lazy val root = project.in(file(".")).autoAggregate.settings(Seq(
  scalaVersion := "3.8.4",
  publish / skip := true,
  commands += Command("printVersion")(BasicCommands.otherCommandParser) {case (state, _) =>
    plugin.projectRefs.foreach(p => println(state.getSetting(p / version)))
    state
  }
))
lazy val plugin = (projectMatrix in file("plugin"))
  .enablePlugins(SbtPlugin)
  .settings(
    name := """sbt-cached-ci""",
    scriptedLaunchOpts ++= Seq("-Xmx512M", "-Dplugin.version=" + version.value, "--enable-native-access=ALL-UNNAMED", "--sun-misc-unsafe-memory-access=allow"),
    scriptedParallelInstances := 2,
    scriptedBatchExecution := true,
    scriptedBufferLog := false,
    // force lowest possible java target
    scalacOptions := Seq(if (scalaBinaryVersion.value.startsWith("3")) "-release:17" else "-target:jvm-1.8"),
    // test with latest sbt versions
    scriptedSbt := (if (scalaBinaryVersion.value.startsWith("3")) "2.0.0" else "1.12.13"),
    sbtTestDirectory := sourceDirectory.value / (if (scalaBinaryVersion.value.startsWith("3")) "sbt2-test" else "sbt1-test"),
    addSbtPlugin("com.github.sbt" % "sbt2-compat" % "0.1.0"),
    cachedCiTestFull := scripted.toTask("").value,
    cachedCiTestQuick := cachedCiTestFull.value,
  )
  .jvmPlatform(scalaVersions = Seq("3.8.4", "2.12.21"))