package com.github.dhsavell.wave.core.bot

import com.github.dhsavell.wave.core.command.CommandFactory
import com.github.dhsavell.wave.core.command.CommandRunner
import com.github.dhsavell.wave.core.util.DslEmbedBuilder
import com.github.dhsavell.wave.core.util.embed
import org.dizitart.no2.Nitrite
import org.slf4j.Logger
import sx.blah.discord.api.IDiscordClient
import sx.blah.discord.api.events.EventSubscriber
import sx.blah.discord.handle.impl.events.ReadyEvent
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

/**
 * Class representing a bot that ties together a CommandManager, ConversationManager, and PermissionManager. Constructor
 * is injectable with the prefix being named "prefix".
 */
class Bot constructor(
    val client: IDiscordClient,
    val logger: Logger,
    val defaultPrefix: String,
    val db: Nitrite,
    commandFactories: List<CommandFactory>
) {
    val commandRunner = CommandRunner(commandFactories)

    fun runForever() {
        client.dispatcher.registerListener(this)
        client.login()
    }

    @EventSubscriber
    @Suppress("unused_parameter")
    fun onReady(event: ReadyEvent) {
        logger.info("Logged in as ${client.ourUser.name}#${client.ourUser.discriminator} (${client.ourUser.stringID})")
        logger.info("Servers: ${client.guilds.size}")
        logger.info("Commands: ${commandRunner.commandFactories.size}")
    }

    @EventSubscriber
    fun onMessageSent(event: MessageReceivedEvent) {
        val message = event.message
        val channel = message.channel

        if (message.author == client.ourUser || channel.isPrivate ||
            !message.content.startsWith(defaultPrefix, ignoreCase = true)
        ) {
            return
        }

        val commandResult = commandRunner.runCommand(message.content.substring(defaultPrefix.length), message, db)
        if (!commandResult.wasSuccessful) {
            channel.sendError(commandResult.description)
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