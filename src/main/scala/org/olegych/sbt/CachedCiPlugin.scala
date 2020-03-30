package org.olegych.sbt

import java.time.Instant

import sbt._
import sbt.Keys._
import sbt.plugins.JvmPlugin

import scala.concurrent.duration._

object CachedCiPlugin extends AutoPlugin {
  override def trigger = allRequirements
  override def requires = JvmPlugin

  object autoImport {
    lazy val cachedCiTestFull = taskKey[Unit]("Full test.")
    lazy val cachedCiTestFullPeriod = settingKey[FiniteDuration]("Period between full tests.")
    lazy val cachedCiTestQuick = taskKey[Unit]("Quick test.")
    lazy val cachedCiTest = taskKey[Unit]("Runs clean and full test if last full test was more than cachedCiTestFullPeriod ago, otherwise runs quick test.")
  }

  import autoImport._

  private case class Token(path: File) {
    path.getParentFile.mkdirs()
    val lastModified = Instant.ofEpochMilli(path.lastModified())
    def valid(period: FiniteDuration) = path.exists() && lastModified.isAfter(Instant.now.minusMillis(period.toMillis))
    def refresh() = {
      path.delete()
      path.createNewFile()
    }
  }
  override lazy val projectSettings = Seq(
    cachedCiTestFull := (Test / test).value,
    cachedCiTestQuick := (Test / testQuick).toTask("").value,
    cachedCiTestFullPeriod := 24.hours,
    cachedCiTest / aggregate := false,
    cachedCiTest := {
      val s = state.value
      val extracted = Project.extract(s)
      import extracted._
      def run(t: TaskKey[_]) = {
        s.log.info(s"Running ${thisProjectRef.value.project} / ${t.key.label}")
        runAggregated(thisProjectRef.value / t, s)
      }
      val testFullToken = Token((if (crossPaths.value) crossTarget.value else target.value) / ".lastCachedCiTestFull")
      s.log.info(s"Last ${cachedCiTest.key.label} was at ${testFullToken.lastModified}")
      if (testFullToken.valid(cachedCiTestFullPeriod.value)) {
        run(cachedCiTestQuick)
      } else {
        val cleanToken = Token(target.value / ".lastCachedCiTestClean")
        if (!cleanToken.valid(cachedCiTestFullPeriod.value)) {
          run(clean)
          cleanToken.refresh()
        }
        run(cachedCiTestFull)
        testFullToken.refresh()
      }
    }
  )
}
