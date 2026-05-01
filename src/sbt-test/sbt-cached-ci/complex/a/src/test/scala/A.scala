import java.io.File

import org.specs2.mutable.Specification

class A extends Specification {
  "a" in {
    new File(s"target/out/jvm/scala-${scala.util.Properties.versionNumberString}/a/A.result").createNewFile()
    ok
  }
}
