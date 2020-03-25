package org.olegych.sbt

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
    lazy val cachedCiTest = taskKey[Unit]("Runs clean and full test if last full test was longer than cachedCiTestFullPeriod ago, otherwise runs quick test.")
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
      val thisProject = thisProjectRef.value
      val token = target.value / ".lastCachedCiTestFull"
      val quick = token.exists() && token.lastModified() > (System.currentTimeMillis() - cachedCiTestFullPeriod.value.toSeconds)
      if (quick) {
        runTask(thisProject / cachedCiTestQuick, s)
      } else {
        runTask(thisProject / clean, s)
        runTask(thisProject / cachedCiTestFull, s)
        token.delete()
        token.createNewFile()
      }
    }
  )
}
