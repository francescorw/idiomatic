package ai.frw.idiomatic.core.flows.instances.addgif

import ai.frw.idiomatic.core.flows.ActivityBase
import ai.frw.idiomatic.core.flows.FlowContext
import ai.frw.idiomatic.data.{GifStorage, GifModel}
import ai.frw.idiomatic.flows.instances.addgif.GifDataModel

class GifWriteActivity[T](override val context: FlowContext[T]) extends ActivityBase(context) {
    def execute(): Boolean = {
        var gifData = context.data("gif").asInstanceOf[GifDataModel]
        var gifTags = context.data("tags").asInstanceOf[String]

        GifStorage.add(GifModel(GifStorage.generateNewId, gifData.animationId, gifData.thumbId, gifTags))
        true
    }
}
