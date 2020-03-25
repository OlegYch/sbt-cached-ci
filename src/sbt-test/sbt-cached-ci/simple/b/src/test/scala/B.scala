import java.io.File

import org.specs2.mutable.Specification

class B extends Specification {
  "b" in {
    Thread.sleep(1000)
    new File("b/target/B.result").createNewFile()
    ok
  }
}
