package ai.frw.idiomatic.core.flows.instances.addgif

import ai.frw.idiomatic.core.flows.{FlowBase, FlowContext, ActivityBase}
import ai.frw.idiomatic.core.flows.activities.WriteTextActivity
import ai.frw.idiomatic.core.flows.PauseFlowActivity
import ai.frw.idiomatic.core.flows.activities.ReadMessageActivity
import ai.frw.idiomatic.common.Emoji
import com.bot4s.telegram.models.Message
import ai.frw.idiomatic.flows.instances.addgif.GifDataModel
import com.typesafe.scalalogging.Logger

class AddGifFlow(override val context: FlowContext[Message]) extends FlowBase(context) {
    val logger = Logger[AddGifFlow]

    setActivities(Seq[ActivityBase[Message]](
        new WriteTextActivity(context, () => s"${Emoji.label} send me the tags"),
        new PauseFlowActivity(context),
        new ReadMessageActivity(context, readTags),
        new WriteTextActivity(context, () => s"${Emoji.camera} send me the gif"),
        new PauseFlowActivity(context),
        new ReadMessageActivity(context, readGif),
        new GifWriteActivity(context),
        new WriteTextActivity(context, () => s"${Emoji.checkmark} gif successfully added")
    ))

    private def readTags(message: Message): Boolean = {
        var msg = message.text.orNull

        if (msg == null || msg.trim.isEmpty) return false

        logger.debug(s"tags: $msg")

        context.data += ("tags" -> msg)
        
        true
    }

    private def readGif(message: Message): Boolean = {
        var animation = message.animation.orNull

        if (animation == null) return false

        var thumb = animation.thumb.orNull
        var defaultThumb = animation.fileId

        if (thumb != null) defaultThumb = thumb.fileId

        logger.debug(s"gif: id: ${animation.fileId} / thumbId: $defaultThumb")        
        
        var gifDataModel = GifDataModel(animation.fileId, defaultThumb)

        context.data += ("gif" -> gifDataModel)
        
        true
    }
}

