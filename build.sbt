enablePlugins(SbtPlugin)
name := """sbt-cached-ci"""

scriptedLaunchOpts ++= Seq("-Xmx1024M", "-Dplugin.version=" + version.value, "--enable-native-access=ALL-UNNAMED", "--sun-misc-unsafe-memory-access=allow")

scriptedBufferLog := false
pluginCrossBuild / sbtVersion := "2.0.0-RC12" //https://github.com/sbt/sbt/issues/5049
scalacOptions := Seq("-release:17")

cachedCiTestFull := scripted.toTask("").value
cachedCiTestQuick := cachedCiTestFull.value

