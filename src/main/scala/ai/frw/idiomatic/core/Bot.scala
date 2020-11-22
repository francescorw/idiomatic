package ai.frw.idiomatic.core

import cats.instances.future._
import cats.syntax.functor._
import com.bot4s.telegram.api.RequestHandler
import com.bot4s.telegram.future.{Polling, TelegramBot}
import com.bot4s.telegram.clients.FutureSttpClient
import com.bot4s.telegram.api.declarative.{Commands, InlineQueries, Messages}
import com.bot4s.telegram.models.Message
import com.softwaremill.sttp.okhttp._

import ai.frw.idiomatic.core.flows.{FlowTelegramContext, FlowBase}
import ai.frw.idiomatic.common.{Emoji, Commands}

import scala.concurrent.Future
import scala.util.Success
import scala.util.Failure
import scala.collection.mutable.Map
import com.bot4s.telegram.methods.SendMessage
import cats.instances.boolean
import com.typesafe.scalalogging.Logger
import ai.frw.idiomatic.core.flows.instances.addgif.AddGifFlow

class Bot(val token: String) extends TelegramBot
    with Polling
    with InlineQueries[Future]
    with Messages[Future]
    with Commands[Future]
    with Authentication {
    
    implicit val backend = OkHttpFutureBackend()
    override val client: RequestHandler[Future] = new FutureSttpClient(token)

    val customLogger = Logger[Bot]
    val userFlows: Map[Long, FlowBase[Message]] = Map()

    onCommand(Commands.start | Commands.commands) { implicit msg =>
        reply(Commands.getAvailableCommands).void
    }

    onCommand(Commands.cancel) { implicit msg =>
        authenticatedOrElse {
            _ => tryCancelFlow(msg)
        } /* else */ {
            _ => replyNotAuthorized(msg)
        }
    }

    onCommand(Commands.addGif) { implicit msg => 
        authenticatedOrElse {
            _ => tryStartFlow(msg, Commands.addGif)
            
        } /* else */ {
            _ => replyNotAuthorized(msg)
        }
    }
    
    onMessage { implicit msg => 
        if (!msg.text.getOrElse("").startsWith("/")) {
            authenticatedOrElse {
                _ => {
                    customLogger.debug("received msg: {}", msg.text.getOrElse(""))

                    val userFlow = userFlows.get(msg.from.get.id).get
                    userFlow.context.lastReceivedMessage = msg

                    if (userFlow != null) {
                        flowNextStep(userFlow)
                    }

                    unit
                }
                
            } /* else */ {
                _ => replyNotAuthorized(msg)
            }
        }

        unit
    }

    onInlineQuery { implicit iq => {
            customLogger.debug("received inline query: {}", iq.query)

            GifBotProxy
            .search(iq.query)
            .onComplete {
                case Success(value) => {
                    customLogger.debug(s"query answered with ${value.length} elements")
                    answerInlineQuery(value)
                }
                case Failure(exception) => customLogger.error(exception.toString)
            }

            unit
        }
    }

    private def replyNotAuthorized(implicit msg: Message): Future[Unit] = {
        val fromUser = msg.from.get
        val stringToLog = s"""
        |received unauthorized msg
        |username: ${fromUser.username.getOrElse("not set")}
        |displayName: ${fromUser.firstName} ${fromUser.lastName.getOrElse((""))}
        |id: ${fromUser.id}
        """
        customLogger.info(stringToLog.stripMargin('|'))

        reply(s"${Emoji.noEntrySign} not authorized").void
    }

    private def tryStartFlow(implicit msg: Message, flowName: String): Future[Unit] = {
        val flow = createFlowFor(msg.from.get.id, flowName)

        if (flow != null) {
            flowNextStep(flow)
        }
        else {
            reply(s"${Emoji.warningSign} please complete the current operation, /cancel to quit")
        }
        unit
    }

    private def tryCancelFlow(implicit msg: Message): Future[Unit] = {
        userFlows.remove(msg.from.get.id)
        reply(s"${Emoji.checkmark} operation cancelled. Type /commands to see what commands are available.").void
    }
    
    private def createFlowFor(userId: Long, flowKey: String): FlowBase[Message] = {
        synchronized {
            if (userFlows.contains((userId))){
                return null
            }

            val flowContext = new FlowTelegramContext(
                userId, 
                (user, txt) => request(SendMessage(user, txt))
            )

            var flowInstance: FlowBase[Message] = null
            flowKey match {
                case Commands.addGif => flowInstance = new AddGifFlow(flowContext)
            }

            if (flowInstance == null) {
                return null
            }

            userFlows += (userId -> flowInstance)

            flowInstance
        }
    }

    private def flowNextStep(flow: FlowBase[Message]) {
        val result = flow.nextStep

        if (!result) {
            synchronized {
                userFlows.remove(flow.context.userId)
            }
        }
    }
}
