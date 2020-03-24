enablePlugins(SbtPlugin)
name := """sbt-cached-ci"""
organization := "org.olegych"
version := "0.1-SNAPSHOT"

bintrayPackageLabels := Seq("sbt", "plugin")
bintrayVcsUrl := Some("""git@github.com:olegych/sbt-cached-ci.git""")

initialCommands in console := """import org.olegych.sbt._"""

scriptedLaunchOpts ++= Seq("-Xmx1024M", "-Dplugin.version=" + version.value)
