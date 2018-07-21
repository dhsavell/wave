package com.github.dhsavell.wave.core.bot

import com.github.dhsavell.wave.core.command.CommandManager
import com.github.dhsavell.wave.core.conversation.ConversationManager
import com.github.dhsavell.wave.core.util.DslEmbedBuilder
import com.github.dhsavell.wave.core.util.embed
import org.mapdb.DB
import org.slf4j.Logger
import sx.blah.discord.api.IDiscordClient
import sx.blah.discord.api.events.EventSubscriber
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent
import sx.blah.discord.handle.obj.IChannel
import sx.blah.discord.handle.obj.IMessage
import java.awt.Color

object BotColors {
    val INFO = Color(0x45a1ff)
    val WARNING = Color(0xffe900)
    val ERROR = Color(0xff0039)
    val SUCCESS = Color(0x30e60b)
}

class Bot(val client: IDiscordClient, val logger: Logger, private val defaultPrefix: String, private val db: DB,
          val commandManager: CommandManager, private val conversationManager: ConversationManager) {
    init {
        val dispatcher = client.dispatcher
        dispatcher.registerListener(this)
    }

    fun runForever() {
        logger.info("Logged in as ${client.ourUser.name}#${client.ourUser.discriminator} (${client.ourUser.stringID})")
        logger.info("Currently in ${client.guilds.size} servers")
    }

    @EventSubscriber
    fun onMessageSent(event: MessageReceivedEvent) {
        val message = event.message

        when {
            message.author == client.ourUser -> return
            conversationManager.isResponse(message) -> conversationManager.handleResponse(message)
            message.content.startsWith(defaultPrefix) -> {
                val commandCall = message.content.substring(defaultPrefix.length)
                val command = commandManager.getCommandFromCall(commandCall)
                if (command != null) {
                    command(this, db, message, commandCall.split(" ").drop(1))
                } else {
                    message.channel.sendError("Unknown command.")
                }
            }
        }
    }
}

fun IChannel.sendEmbed(initBlock: DslEmbedBuilder.() -> Unit): IMessage {
    return sendMessage(embed(initBlock))
}


fun IChannel.sendInfo(message: String): IMessage = sendEmbed {
    title { message }
    color { BotColors.INFO }
}

fun IChannel.sendWarning(message: String): IMessage = sendEmbed {
    title { "**Warning**: $message" }
    color { BotColors.WARNING }
}

fun IChannel.sendError(message: String): IMessage = sendEmbed {
    title { "**Error**: $message" }
    color { BotColors.ERROR }
}

fun IChannel.sendSuccess(message: String): IMessage = sendEmbed {
    title { "**Success**: $message" }
    color { BotColors.SUCCESS }
}