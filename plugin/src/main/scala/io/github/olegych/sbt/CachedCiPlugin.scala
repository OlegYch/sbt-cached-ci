package io.github.olegych.sbt

import sbt.*
import sbt.Keys.*
import sbt.Tags.Tag
import sbt.plugins.JvmPlugin
import sbtcompat.PluginCompat.*

import java.io.FileNotFoundException
import java.time.Instant
import scala.concurrent.duration.*

object CachedCiPlugin extends AutoPlugin {
  override def trigger = allRequirements

  override def requires = JvmPlugin

  object autoImport {
    @transient
    lazy val cachedCiTestFull = taskKey[Unit]("Full test.")
    @transient
    lazy val cachedCiTestFullToken = taskKey[String]("Run full tests if this changed.")
    lazy val cachedCiTestFullPeriod = settingKey[FiniteDuration]("Period between full tests.")
    @transient
    lazy val cachedCiTestQuick = taskKey[Unit]("Quick test.")
    lazy val cachedCiTest = taskKey[Unit]("Runs clean and full test if last full test was more than cachedCiTestFullPeriod ago, otherwise runs quick test.")
  }

  import autoImport.*

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

  private val sbt2 = scala.util.Properties.versionNumberString.startsWith("3")
  private val testFull: TaskKey[?] = (if (sbt2) TaskKey[TestResult]("testFull") else TaskKey[Unit]("test"))

  override lazy val projectSettings = Seq(
    cachedCiTestFull := (Test / testFull).value,
    cachedCiTestQuick := (Test / testQuick).toTask("").value,
    cachedCiTestFullToken := {
      implicit val conv: xsbti.FileConverter = fileConverter.value
      val deps = (Runtime / externalDependencyClasspath).value
      deps.map(toFile).map(_.toString).sorted.mkString
    },
    cachedCiTestFullPeriod := 24.hours,
    cachedCiTest := Def.taskDyn {
      val log = state.value.log

      def run[T](t: TaskKey[T]) = Def.taskDyn {
        val label = s"${thisProjectRef.value.project} / ${t.key.label}"
        Def.sequential(
          Def.task(log.info(s"Running $label")),
          t,
        )
      }

      val cachedCiTestFullTokenValue = cachedCiTestFullToken.value
      val testFullToken = Token((if (crossPaths.value) crossTarget.value else target.value) / ".lastCachedCiTestFull", cachedCiTestFullTokenValue)
      log.info(s"Last ${thisProjectRef.value.project} / ${cachedCiTest.key.label} was at ${testFullToken.lastModified}, token value changed: ${testFullToken.valueChanged}")
      if (testFullToken.valid(cachedCiTestFullPeriod.value)) run(cachedCiTestQuick)
      else {
        val cleanToken = Token(target.value / ".lastCachedCiTestClean", "")
        val runClean = if (cleanToken.valid(cachedCiTestFullPeriod.value)) Nil else List(
          run(clean),
          Def.task(cleanToken.refresh()),
        )
        Def.sequential(runClean ++ List(
          run(cachedCiTestFull),
          Def.task(testFullToken.refresh()),
        ))
      }
    }.value
  )
}
