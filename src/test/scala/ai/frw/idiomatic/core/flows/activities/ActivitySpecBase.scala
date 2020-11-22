package ai.frw.idiomatic.test.core.flows.activities

import ai.frw.idiomatic.test.UnitSpec
import cats.instances.unit
import scala.concurrent.Future

class ActivitySpecBase extends UnitSpec {
    private var stdOutput = "" 
    val context = new FlowTestContext(1, (userId, msg) => writeToStdOutput((msg)))
    
    private def writeToStdOutput(text: String): Future[Unit] = {
        stdOutput = text
        Future.successful(unit)
    }

    def readFromStdOutput(): String = {
        stdOutput
    }
}
