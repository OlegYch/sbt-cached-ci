import java.io.File

import org.specs2.mutable.Specification

class B extends Specification {
  "b" in {
    new File(s"b/target/scala-${scala.util.Properties.versionNumberString.reverse.dropWhile(_ != '.').tail.reverse}/B.result").createNewFile()
    ok
  }
}
