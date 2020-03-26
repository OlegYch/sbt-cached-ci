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

  override lazy val projectSettings = Seq(
    cachedCiTestFull := (Test / test).value,
    cachedCiTestQuick := (Test / testQuick).toTask("").value,
    cachedCiTestFullPeriod := 24.hours,
    cachedCiTest := {
      val s = state.value
      val extracted = Project.extract(s)
      import extracted._
      val thisProjectRef_ = thisProjectRef.value
      val thisProject_ = thisProject.value
      val token = target.value / ".lastCachedCiTestFull"
      val lastRun = Instant.ofEpochMilli(token.lastModified())
      s.log.info(s"Last ${thisProject_.id} / ${cachedCiTest.key.label} was at ${lastRun}")
      val quick = token.exists() && lastRun.isAfter(Instant.now.minusMillis(cachedCiTestFullPeriod.value.toMillis))
      if (quick) {
        s.log.info(s"Running ${thisProject_.id} / ${cachedCiTestQuick.key.label}")
        runTask(thisProjectRef_ / cachedCiTestQuick, s)
      } else {
        s.log.info(s"Running ${thisProject_.id} / ${clean.key.label}")
        runTask(thisProjectRef_ / clean, s)
        s.log.info(s"Running ${thisProject_.id} / ${cachedCiTestFull.key.label}")
        runTask(thisProjectRef_ / cachedCiTestFull, s)
        token.delete()
        token.createNewFile()
      }
    }
  )
}
