enablePlugins(SbtPlugin)
name := """sbt-cached-ci"""
organization := "org.olegych"

bintrayPackageLabels := Seq("sbt", "plugin")
bintrayVcsUrl := Some("""git@github.com:olegych/sbt-cached-ci.git""")

scriptedLaunchOpts ++= Seq("-Xmx1024M", "-Dplugin.version=" + version.value)

scriptedBufferLog := false
pluginCrossBuild / sbtVersion := "1.2.8" //https://github.com/sbt/sbt/issues/5049

Test / test := scripted.toTask("").value
cachedCiTestQuick := cachedCiTestFull.value