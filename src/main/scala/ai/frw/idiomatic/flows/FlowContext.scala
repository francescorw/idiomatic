package ai.frw.idiomatic.core.flows

import scala.concurrent.Future
import com.bot4s.telegram.models.Message
import scala.collection.mutable.Map

abstract class FlowContext[T](val userId: Long, val sendMessage: (Long, String) => Future[T]) {
    var lastReceivedMessage: T
    var data: Map[String, Any] = Map()
}
