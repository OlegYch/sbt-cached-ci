enablePlugins(SbtPlugin)
name := """sbt-cached-ci"""

scriptedLaunchOpts ++= Seq("-Xmx1024M", "-Dplugin.version=" + version.value, "--enable-native-access=ALL-UNNAMED", "--sun-misc-unsafe-memory-access=allow")

scriptedBufferLog := false
// force lowest possible java target
scalacOptions := Seq(if (scalaBinaryVersion.value.startsWith("3")) "-release:17" else "-target:jvm-1.8")
// test with latest sbt versions
scriptedSbt := (if (scalaBinaryVersion.value.startsWith("3")) "2.0.0" else "1.12.13")
crossScalaVersions := Seq(scalaVersion.value, "2.12.21")
addSbtPlugin("com.github.sbt" % "sbt2-compat" % "0.1.0")

cachedCiTestFull := scripted.toTask("").value
cachedCiTestQuick := cachedCiTestFull.value

