package com.github.dhsavell.wave.core.bot

import com.github.dhsavell.wave.core.command.CommandManager
import com.github.dhsavell.wave.core.conversation.ConversationManager
import sx.blah.discord.api.IDiscordClient
import sx.blah.discord.api.events.EventSubscriber
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent
import sx.blah.discord.handle.obj.IChannel
import sx.blah.discord.handle.obj.IMessage

class Bot(private val client: IDiscordClient, private val defaultPrefix: String,
          private val commandManager: CommandManager, private val conversationManager: ConversationManager) {
    init {
        val dispatcher = client.dispatcher
        dispatcher.registerListener(this)
    }

    fun sendInfo(destination: IChannel, message: String): IMessage {
        return destination.sendMessage("`Info:` $message")
    }

    fun sendWarning(destination: IChannel, message: String): IMessage {
        return destination.sendMessage("`Warning:` $message")
    }

    fun sendError(destination: IChannel, message: String): IMessage {
        return destination.sendMessage("`Error:` $message")
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
                    command.call(this, message, commandCall.split(" ").drop(1).toTypedArray())
                } else {
                    sendError(message.channel, "Unknown command.")
                }
            }
        }
    }
}