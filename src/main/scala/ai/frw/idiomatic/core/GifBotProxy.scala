package ai.frw.idiomatic.core

import com.bot4s.telegram.models.InlineQueryResultGif
import ai.frw.idiomatic.data.GifStorage
import scala.concurrent.Future

object GifBotProxy {
  def search(query: String): Future[Seq[InlineQueryResultGif]] = {
    Future.successful(
      GifStorage.
      get(query).
      map(item => new InlineQueryResultGif(item.id, item.url, thumbUrl = item.thumbUrl)).
      toSeq
    )
  }
}
