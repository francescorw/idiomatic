package ai.frw.idiomatic.core.flows.activities

import ai.frw.idiomatic.core.flows.{FlowContext, ActivityBase}

class WriteTextActivity[T](override val context: FlowContext[T], val getTextToSend: () => String) extends ActivityBase(context) {
    def execute(): Boolean = {
        context.sendMessage(context.userId, getTextToSend())
        true
    }
}
