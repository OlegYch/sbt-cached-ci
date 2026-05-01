enablePlugins(SbtPlugin)
name := """sbt-cached-ci"""

val javaVersion = scala.util.Properties.javaVersion.toInt
scriptedLaunchOpts ++= Seq("-Xmx1024M", "-Dplugin.version=" + version.value)
scriptedLaunchOpts ++= (if (javaVersion >=24) Seq("--enable-native-access=ALL-UNNAMED", "--sun-misc-unsafe-memory-access=allow") else Nil)

scriptedBufferLog := false
pluginCrossBuild / sbtVersion := "2.0.0-RC12" //https://github.com/sbt/sbt/issues/5049
scalacOptions := Seq("-release:17")

cachedCiTestFull := scripted.toTask("").value
cachedCiTestQuick := cachedCiTestFull.value

