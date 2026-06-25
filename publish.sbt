ThisBuild / credentials += Credentials(Path.userHome / ".sbt" / "sonatype_central_credentials")
// todo if ivy is disabled publishLocal for plugin2_12 omits cross suffixes, disable only on release or fix in sbt
//useIvy := false
//commands += Command("disableIvy")(BasicCommands.otherCommandParser) {case (state, _) =>
//  state.appendWithSession(Seq(useIvy := false))
//}
ThisBuild / releaseUseGlobalVersion := false
ThisBuild / sbtPluginPublishLegacyMavenStyle := false
ThisBuild / organization := "io.github.olegych"
ThisBuild / organizationName := "OlegYch"
ThisBuild / organizationHomepage := Some(url("https://github.com/OlegYch"))

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/OlegYch/sbt-cached-ci"),
    "scm:git@github.com:OlegYch/sbt-cached-ci.git"
  )
)
ThisBuild / developers := List(
  Developer(
    id    = "OlegYch",
    name  = "Aleh Aleshka",
    email = "oleglbch@gmail.com",
    url   = url("https://github.com/OlegYch")
  )
)

ThisBuild / description := "Incremental sbt builds for CI environments."
ThisBuild / licenses := Seq("BSD-style" -> url("http://www.opensource.org/licenses/bsd-license.php"))
ThisBuild / homepage := Some(url("https://github.com/OlegYch/sbt-cached-ci"))

// Remove all additional repository other than Maven Central from POM
ThisBuild / pomIncludeRepository := { _ => false }
ThisBuild / publishMavenStyle := true

ThisBuild / publishTo := {
  val centralSnapshots = "https://central.sonatype.com/repository/maven-snapshots/"
  if (isSnapshot.value) Some("central-snapshots" at centralSnapshots)
  else localStaging.value
}

import ReleaseTransformations._
ThisBuild / versionScheme := Some("early-semver")
releaseProcess := Seq[ReleaseStep](
//  checkSnapshotDependencies,
  inquireVersions,
//  runClean,
//  runTest,
//  releaseStepCommandAndRemaining("disableIvy"),
  setReleaseVersion,
  releaseStepCommandAndRemaining("printVersion"),
//  commitReleaseVersion,
//  tagRelease,
//  releaseStepCommandAndRemaining("publishSigned"),
//  releaseStepCommand("sonaRelease"),
//  setNextVersion,
//  commitNextVersion,
//  pushChanges
)
