import java.io.File

import org.specs2.mutable.Specification

class A extends Specification {
  "a" in {
    new File(s"a/target/scala-${scala.util.Properties.versionNumberString.reverse.dropWhile(_ != '.').tail.reverse}/A.result").createNewFile()
    ok
  }
}
