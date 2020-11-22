package ai.frw.idiomatic

import ai.frw.idiomatic.core.Bot
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import com.typesafe.scalalogging.Logger

object Main extends App {
  val logger = Logger(Main.getClass())

  logger.info("bot started")

  val token = sys.env.getOrElse("TELEGRAM_TOKEN", null)

  if (token == null || token.isEmpty) {
    logger.error("no token provided")
    sys.exit
  }
  val bot = new Bot(token)
  
  Await.result(bot.run(), Duration.Inf)

  logger.info("bot stopped")
}
