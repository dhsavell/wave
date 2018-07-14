package com.github.dhsavell.wave.core.bot

import com.github.dhsavell.wave.core.command.Command
import com.github.dhsavell.wave.core.command.UserCommand
import com.github.dhsavell.wave.core.conversation.Conversation
import net.java.sezpoz.Index
import sx.blah.discord.api.IDiscordClient
import sx.blah.discord.api.events.EventSubscriber
import sx.blah.discord.handle.impl.events.ReadyEvent
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent
import sx.blah.discord.handle.obj.IChannel
import sx.blah.discord.handle.obj.IMessage

class ModularBot(private val client: IDiscordClient, private val defaultPrefix: String) {
    private val commands = Index.load(Command::class.java, UserCommand::class.java).associateBy({
        it.annotation()!!
    }, {
        it.instance()!!
    })

    private var conversations: List<Conversation> = ArrayList()

    init {
        client.dispatcher.registerListener(this)
    }

    fun runForever() {
        client.login()
    }

    fun sendInfo(destination: IChannel, content: String): IMessage {
        return destination.sendMessage(":large_blue_circle: **Info**: $content")
    }

    fun sendError(destination: IChannel, content: String): IMessage {
        return destination.sendMessage(":large_red_circle: **Error**: $content")
    }

    /**
     * Attempts to continue a conversation.
     * @return Whether or not the conversation was continued.
     */
    fun continueConversation(message: IMessage): Boolean {
        val relevantConversations = conversations.filter { conversation ->
            conversation.isMessageRelevant(message)
        }

        if (relevantConversations.isNotEmpty()) {
            relevantConversations.map { conversation ->
                val wasSuccess = conversation.onMessage(message)
                if (wasSuccess) {
                    conversations = conversations.filter { otherConversation -> otherConversation != conversation }
                }
            }

            return true
        }

        return false
    }

    @EventSubscriber
    fun onReady(event: ReadyEvent) {
        TODO("Not implemented!")
    }

    @EventSubscriber
    fun onMessageReceived(event: MessageReceivedEvent) {
        val message = event.message

        if (message.author == client.ourUser) {
            return
        }

        if (continueConversation(event.message)) {
            return
        }

        if (message.content.startsWith(defaultPrefix, true)) {
            val commandCall = message.content.substring(defaultPrefix.length)
            val commandParts = commandCall.split(" ")
            val commandName = commandParts[0].toLowerCase()

            commands.filter { entry ->
                entry.key.name == commandName || entry.key.aliases.contains(commandName)
            }.filter { entry ->
                entry.value.isUsable(this, message)
            }.forEach { entry ->
                entry.value.call(this, message, "")
            }
        }
    }
}