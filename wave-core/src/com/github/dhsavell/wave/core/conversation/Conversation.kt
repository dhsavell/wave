package com.github.dhsavell.wave.core.conversation

import com.github.dhsavell.wave.core.bot.ModularBot
import sx.blah.discord.handle.obj.IChannel
import sx.blah.discord.handle.obj.IMessage
import sx.blah.discord.handle.obj.IUser

/**
 * Represents an ongoing conversation between this bot and a user.
 * @param bot Bot hosting the conversation.
 * @param user User having a conversation with the bot.
 */
abstract class Conversation(private val bot: ModularBot,
                            private val message: IMessage,
                            private val user: IUser = message.author,
                            private val channel: IChannel = message.channel) {
    /**
     * Handles the next message sent by the user in this conversation.
     *
     * @param message Message sent by the other user in the conversation.
     * @return Whether or not the conversation should continue.
     */
    abstract fun onMessage(message: IMessage): Boolean

    /**
     * Determines whether or not a given message is relevant to this conversation.
     *
     * @param message Message to test.
     * @return Whether or not the message is relevant and onMessage should be called.
     */
    fun isMessageRelevant(message: IMessage): Boolean {
        return message.author == user && message.channel == channel
    }
}