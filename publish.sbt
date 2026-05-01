credentials += Credentials(Path.userHome / ".sbt" / "sonatype_central_credentials")
useIvy := false
releaseUseGlobalVersion := false
sbtPluginPublishLegacyMavenStyle := false
organization := "io.github.olegych"
organizationName := "OlegYch"
organizationHomepage := Some(url("https://github.com/OlegYch"))

scmInfo := Some(
  ScmInfo(
    url("https://github.com/OlegYch/sbt-cached-ci"),
    "scm:git@github.com:OlegYch/sbt-cached-ci.git"
  )
)
developers := List(
  Developer(
    id    = "OlegYch",
    name  = "Aleh Aleshka",
    email = "oleglbch@gmail.com",
    url   = url("https://github.com/OlegYch")
  )
)

description := "Incremental sbt builds for CI environments."
licenses := Seq("BSD-style" -> url("http://www.opensource.org/licenses/bsd-license.php"))
homepage := Some(url("https://github.com/OlegYch/sbt-cached-ci"))

// Remove all additional repository other than Maven Central from POM
pomIncludeRepository := { _ => false }
publishMavenStyle := true

publishTo := {
  val centralSnapshots = "https://central.sonatype.com/repository/maven-snapshots/"
  if (isSnapshot.value) Some("central-snapshots" at centralSnapshots)
  else localStaging.value
}

import ReleaseTransformations._
versionScheme := Some("early-semver")
releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  releaseStepCommandAndRemaining("publishSigned"),
  releaseStepCommand("sonaRelease"),
  setNextVersion,
  commitNextVersion,
  pushChanges
)
