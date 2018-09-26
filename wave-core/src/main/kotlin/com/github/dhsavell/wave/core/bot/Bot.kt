package com.github.dhsavell.wave.core.bot

import com.github.dhsavell.wave.core.command.CommandFactory
import com.github.dhsavell.wave.core.command.CommandRunner
import com.github.dhsavell.wave.core.util.sendError
import org.dizitart.no2.Nitrite
import org.slf4j.Logger
import sx.blah.discord.api.IDiscordClient
import sx.blah.discord.api.events.EventSubscriber
import sx.blah.discord.handle.impl.events.ReadyEvent
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent

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