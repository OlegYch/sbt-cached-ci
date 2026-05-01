import java.io.File

import org.specs2.mutable.Specification

class B extends Specification {
  "b" in {
    new File(s"target/out/jvm/scala-${scala.util.Properties.versionNumberString}/b/B.result").createNewFile()
    ok
  }
}
