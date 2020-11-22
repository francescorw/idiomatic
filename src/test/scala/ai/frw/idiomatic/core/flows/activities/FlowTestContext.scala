package ai.frw.idiomatic.test.core.flows.activities

import ai.frw.idiomatic.core.flows.FlowContext
import scala.concurrent.Future
import cats.instances.unit

class FlowTestContext(override val userId: Long, override val sendMessage: (Long, String) => Future[Unit]) extends FlowContext[Unit](userId, sendMessage) {
    override var lastReceivedMessage: Unit = unit
}
