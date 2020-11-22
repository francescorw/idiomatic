package ai.frw.idiomatic.core.flows

import scala.concurrent.Future
import com.bot4s.telegram.models.Message
import scala.collection.mutable.Map

class FlowTelegramContext(override val userId: Long, override val sendMessage: (Long, String) => Future[Message]) extends FlowContext(userId, sendMessage) {
    override var lastReceivedMessage: Message = null
}
