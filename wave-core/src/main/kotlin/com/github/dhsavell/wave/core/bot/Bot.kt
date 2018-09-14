package com.github.dhsavell.wave.core.bot

import com.github.dhsavell.wave.core.command.CommandManager
import com.github.dhsavell.wave.core.conversation.ConversationManager
import com.github.dhsavell.wave.core.permission.PermissionManager
import com.github.dhsavell.wave.core.util.DslEmbedBuilder
import com.github.dhsavell.wave.core.util.embed
import org.mapdb.DB
import org.slf4j.Logger
import sx.blah.discord.api.IDiscordClient
import sx.blah.discord.api.events.EventSubscriber
import sx.blah.discord.handle.impl.events.ReadyEvent
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent
import sx.blah.discord.handle.obj.IChannel
import sx.blah.discord.handle.obj.IMessage
import java.awt.Color
import javax.inject.Inject
import javax.inject.Named

object BotColors {
    val INFO = Color(0x45a1ff)
    val WARNING = Color(0xffe900)
    val ERROR = Color(0xff0039)
    val SUCCESS = Color(0x30e60b)
}

/**
 * Class representing a bot that ties together a CommandManager, ConversationManager, and PermissionManager. Constructor
 * is injectable with the prefix being named "prefix".
 */
open class Bot @Inject constructor(
    val client: IDiscordClient,
    val logger: Logger,
    @Named("prefix") private val defaultPrefix: String,
    val db: DB,
    val commandManager: CommandManager,
    val conversationManager: ConversationManager,
    val permissionManager: PermissionManager
) {
    fun runForever() {
        client.dispatcher.registerListener(this)
        client.login()
    }

    @EventSubscriber
    @Suppress("unused_parameter")
    fun onReady(event: ReadyEvent) {
        logger.info("Logged in as ${client.ourUser.name}#${client.ourUser.discriminator} (${client.ourUser.stringID})")
        logger.info("Servers: ${client.guilds.size}")
        logger.info("Commands: ${commandManager.commands.size}")
    }

    @EventSubscriber
    fun onMessageSent(event: MessageReceivedEvent) {
        val message = event.message
        val channel = message.channel

        if (message.author == client.ourUser) {
            return
        }

        when {
            channel.isPrivate -> channel.sendError("Wave is currently not available for direct messages.")
            conversationManager.isResponse(message) -> conversationManager.handleResponse(message)
            message.content.startsWith(defaultPrefix, true) -> {
                val commandCall = message.content.substring(defaultPrefix.length)
                val command = commandManager.getCommandFromCall(commandCall)
                val commandArguments = commandCall.split(" ").drop(1)
                if (command != null) {
                    if (permissionManager.userCanInvoke(command, message.author, message.guild)) {
                        command(this, message, commandArguments)
                    } else {
                        channel.sendError("You don't have permission to use this command.")
                    }
                } else {
                    channel.sendError("Unknown command.")
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