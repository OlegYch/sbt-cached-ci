lazy val root = project.in(file(".")).autoAggregate.settings(Seq(
  scalaVersion := "3.9.0",
  publish / skip := true,
))
lazy val plugin = (projectMatrix in file("plugin"))
  .enablePlugins(SbtPlugin)
  .settings(
    name := """sbt-cached-ci""",
    scriptedLaunchOpts ++= Seq("-Xmx512M", "-Dplugin.version=" + version.value, "--enable-native-access=ALL-UNNAMED") ++
      (if (scala.util.Properties.isJavaAtLeast("23")) Seq("--sun-misc-unsafe-memory-access=allow") else Nil),
    scriptedParallelInstances := 2,
    scriptedBatchExecution := true,
    scriptedBufferLog := false,
    // force lowest possible java target
    scalacOptions := (if (scalaBinaryVersion.value.startsWith("3")) Seq("-release:17") else Seq("-Xsource:3", "-release:8")),
    // build against lowest possible sbt
    pluginCrossBuild / sbtVersion := (if (scalaBinaryVersion.value.startsWith("3")) "2.1.0" else "1.5.8"),
    // test with latest supported sbt versions
    scriptedSbt := (if (scalaBinaryVersion.value.startsWith("3")) "2.1.0" else "1.13.0"),
    sbtTestDirectory := sourceDirectory.value / (if (scalaBinaryVersion.value.startsWith("3")) "sbt2-test" else "sbt1-test"),
    addSbtPlugin("com.github.sbt" % "sbt2-compat" % "0.1.0"),
    cachedCiTestFull := scripted.toTask("").value,
    cachedCiTestQuick := cachedCiTestFull.value,
    Test / testFull := Def.uncached((Test / testFull).dependsOn(cachedCiTestFull).value),
  )
  .jvmPlatform(scalaVersions = Seq("3.9.0", "2.12.21"))
