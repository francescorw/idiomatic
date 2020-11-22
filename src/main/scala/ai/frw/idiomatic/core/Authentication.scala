package ai.frw.idiomatic.core

import com.bot4s.telegram.models.{Message, User}
import com.bot4s.telegram.api.declarative._
import scala.concurrent.Future

trait Authentication {
    val allowed = sys.env.getOrElse("IDM_ADMINS", "").split(',').map(u => u.trim).toSet

    def authenticatedOrElse(ok: Action[Future, Message])
        (deny: Action[Future, Message])
        (implicit msg: Message): Future[Unit] = {
        if (isAuthenticated(msg.from.get))
            ok(msg)
        else
            deny(msg)
    }

    def isAuthenticated(user: User): Boolean = allowed.synchronized {
        allowed.contains(user.username.getOrElse(""))
    }
}
