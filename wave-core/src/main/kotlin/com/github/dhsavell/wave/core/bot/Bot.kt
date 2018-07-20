package com.github.dhsavell.wave.core.bot

import com.github.dhsavell.wave.core.command.CommandManager
import com.github.dhsavell.wave.core.conversation.ConversationManager
import com.github.dhsavell.wave.core.util.DslEmbedBuilder
import com.github.dhsavell.wave.core.util.embed
import org.mapdb.DB
import sx.blah.discord.api.IDiscordClient
import sx.blah.discord.api.events.EventSubscriber
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent
import sx.blah.discord.handle.obj.IChannel
import sx.blah.discord.handle.obj.IMessage
import java.awt.Color

class Bot(private val client: IDiscordClient, private val defaultPrefix: String, private val db: DB,
          private val commandManager: CommandManager, private val conversationManager: ConversationManager) {
    init {
        val dispatcher = client.dispatcher
        dispatcher.registerListener(this)
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
    color { Color(0x45a1ff) }
}

fun IChannel.sendWarning(message: String): IMessage = sendEmbed {
    title { "**Warning**: $message" }
    color { Color(0xffe900) }
}

fun IChannel.sendError(message: String): IMessage = sendEmbed {
    title { "**Error**: $message" }
    color { Color(0xff0039) }
}

fun IChannel.sendSuccess(message: String): IMessage = sendEmbed {
    title { "**Success**: $message" }
    color { Color(0x30e60b) }
}