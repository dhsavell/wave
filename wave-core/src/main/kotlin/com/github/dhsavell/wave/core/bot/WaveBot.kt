package com.github.dhsavell.wave.core.bot

import com.github.dhsavell.wave.core.command.Command
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

object StatusColors {
    val INFO = Color(0x45a1ff)
    val WARNING = Color(0xffe900)
    val ERROR = Color(0xff0039)
    val SUCCESS = Color(0x30e60b)
}

/**
 * A Discord bot supporting command execution, interactive conversations, data storage, and a permission system.
 */
open class WaveBot(
    private val client: IDiscordClient,
    private val logger: Logger,
    private val defaultCommandPrefix: String,
    val db: DB,
    commands: List<Command>
) {
    val commandManager = CommandManager(commands)
    val conversationManager = ConversationManager()
    val permissionManager = PermissionManager(db)

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
            message.content.startsWith(defaultCommandPrefix, true) -> {
                val commandCall = message.content.substring(defaultCommandPrefix.length)
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
    color { StatusColors.INFO }
}

fun IChannel.sendWarning(message: String): IMessage = sendEmbed {
    title { "**Warning**: $message" }
    color { StatusColors.WARNING }
}

fun IChannel.sendError(message: String): IMessage = sendEmbed {
    title { "**Error**: $message" }
    color { StatusColors.ERROR }
}

fun IChannel.sendSuccess(message: String): IMessage = sendEmbed {
    title { "**Success**: $message" }
    color { StatusColors.SUCCESS }
}