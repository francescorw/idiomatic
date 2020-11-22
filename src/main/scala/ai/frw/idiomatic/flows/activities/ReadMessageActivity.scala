package ai.frw.idiomatic.core.flows.activities

import ai.frw.idiomatic.core.flows.ActivityBase
import ai.frw.idiomatic.core.flows.FlowContext

class ReadMessageActivity[T](override val context: FlowContext[T], val importantStuffArchiver: T => Boolean) extends ActivityBase(context) {
    def execute(): Boolean = {
        importantStuffArchiver(context.lastReceivedMessage)
    }
}
