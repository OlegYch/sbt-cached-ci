import java.io.File

import org.specs2.mutable.Specification

class A extends Specification {
  "a" in {
    new File("a/target/A.result").createNewFile()
    ok
  }
}
