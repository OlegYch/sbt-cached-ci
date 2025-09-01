package io.github.olegych.sbt

import java.time.Instant
import sbt.Keys.*
import sbt.Tags.Tag
import sbt.*
import sbt.plugins.JvmPlugin

import java.io.FileNotFoundException
import scala.concurrent.duration.*

object CachedCiPlugin extends AutoPlugin {
  override def trigger = allRequirements

  override def requires = JvmPlugin

  object autoImport {
    lazy val cachedCiTestFull = taskKey[Unit]("Full test.")
    lazy val cachedCiTestFullToken = taskKey[String]("Run full tests if this changed.")
    lazy val cachedCiTestFullPeriod = settingKey[FiniteDuration]("Period between full tests.")
    lazy val cachedCiTestQuick = taskKey[Unit]("Quick test.")
    lazy val cachedCiTest = taskKey[Unit]("Runs clean and full test if last full test was more than cachedCiTestFullPeriod ago, otherwise runs quick test.")
  }

  import autoImport._

  private case class Token(path: File, value: String) {
    val lastModified = Instant.ofEpochMilli(path.lastModified())
    val lastValue = try IO.read(path) catch {
      case e: FileNotFoundException => ""
    }
    val valueChanged = path.exists() && lastValue != value

    def valid(period: FiniteDuration) = !valueChanged && lastModified.isAfter(Instant.now.minusMillis(period.toMillis))

    def refresh() = {
      path.getParentFile.mkdirs()
      path.delete()
      IO.write(path, value)
    }
  }

  val CachedCiTest = Tag("CachedCiTest")
  override lazy val projectSettings = Seq(
    cachedCiTestFull := (Test / test).value,
    cachedCiTestQuick := (Test / testQuick).toTask("").value,
    cachedCiTestFullToken := (Runtime / fullClasspath).value.mkString,
    cachedCiTestFullPeriod := 24.hours,
    concurrentRestrictions += Tags.exclusive(CachedCiTest),
    cachedCiTest := Def.task {
      val s = state.value
      val extracted = Project.extract(s)
      import extracted._
      def run(t: TaskKey[_]): Unit = {
        val label = s"${thisProjectRef.value.project} / ${t.key.label}"
        s.log.info(s"Running $label")
        runTask(thisProjectRef.value / t, s)
      }

      val cachedCiTestFullTokenValue = cachedCiTestFullToken.value
      val testFullToken = Token((if (crossPaths.value) crossTarget.value else target.value) / ".lastCachedCiTestFull", cachedCiTestFullTokenValue)
      s.log.info(s"Last ${thisProjectRef.value.project} / ${cachedCiTest.key.label} was at ${testFullToken.lastModified}, token value changed: ${testFullToken.valueChanged}")
      if (testFullToken.valid(cachedCiTestFullPeriod.value)) {
        run(cachedCiTestQuick)
      } else {
        val cleanToken = Token(target.value / ".lastCachedCiTestClean", "")
        if (!cleanToken.valid(cachedCiTestFullPeriod.value)) {
          run(clean)
          cleanToken.refresh()
        }
        run(cachedCiTestFull)
        testFullToken.refresh()
      }
    }.tag(CachedCiTest).value
  )
}
