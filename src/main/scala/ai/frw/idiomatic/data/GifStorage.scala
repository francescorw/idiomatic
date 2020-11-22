package ai.frw.idiomatic.data

import com.redis._
import com.typesafe.scalalogging.Logger
import play.api.libs.json.Json
import ai.frw.idiomatic.utils.Metaphone
import scala.util.Random

object GifStorage {
    val logger = Logger(GifStorage.getClass())
    val idLength = 6
    val redisHost = sys.env.getOrElse("REDIS_HOST", null)
    val redisPort = sys.env.getOrElse("REDIS_PORT", 0)

    if (redisHost == null || redisHost.isEmpty) {
        throw new IllegalArgumentException("redisHost")
    }

    if (redisPort == 0) {
        throw new IllegalArgumentException("redisPort")
    }

    implicit val gifModelReads = Json.reads[GifModel]
    implicit val gifModelWrites = Json.writes[GifModel]

    val gifHsetName = "gifstorage:gifs"
    val tagSetPrefix = "gifstorage:tags:"

    val redisClient = new RedisClient(redisHost, redisPort.toString.toInt)

    def generateNewId(): String = {
        Random.alphanumeric.take(idLength).mkString("")
    }

    def add(model: GifModel) {
        redisClient.hset("gifstorage:gifs", model.id, Json.stringify(Json.toJson(model)))
        
        val metaphones = model.tags.split(" ").map(t => Metaphone.encode(t))

        metaphones.foreach(mp => redisClient.sadd(s"$tagSetPrefix$mp", model.id))
    }

    def get(tag: String): Set[GifModel] = {
        val metaphones = tag.split(" ").map(t => Metaphone.encode(t))

        metaphones.map(metaphone => redisClient.smembers(s"$tagSetPrefix$metaphone").get).
        flatten.
        map(gifId => redisClient.hget(gifHsetName, gifId.get).get).
        map(serializedValue => Json.parse(serializedValue).as[GifModel]).toSet
    }
}
