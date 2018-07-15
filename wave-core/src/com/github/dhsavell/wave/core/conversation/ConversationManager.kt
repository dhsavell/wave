package com.github.dhsavell.wave.core.conversation

import com.github.dhsavell.wave.core.bot.ModularBot
import sx.blah.discord.handle.obj.IChannel
import sx.blah.discord.handle.obj.IMessage
import sx.blah.discord.handle.obj.IUser

data class ConversationProgress(var progress: List<ConversationPrompt>, val location: IChannel, val participant: IUser,
                                val onCompleted: (Conversation) -> Unit)

const val MAX_CONVERSATION_LENGTH = 20

/**
 * A class for managing multiple ongoing Conversations at once.
 */
class ConversationManager(private val bot: ModularBot) {
    var activeConversations: List<ConversationProgress> = listOf()

    /**
     * Starts a conversation with a given prompt.
     * @param start First conversation prompt.
     * @param location Channel the conversation is taking place in.
     * @param participant User participating in the conversation.
     * @param onCompleted Callback for when the conversation is finished.
     */
    fun beginConversation(start: ConversationPrompt, location: IChannel, participant: IUser,
                          onCompleted: (Conversation) -> Unit) {
        start.sendPrompt(location, participant)
        activeConversations += ConversationProgress(listOf(start), location, participant, onCompleted)
    }

    /**
     * Determines whether or not a message is a conversation response.
     * @param message Message to test.
     * @return Whether or not the message is part of a conversation.
     */
    fun isConversationResponse(message: IMessage): Boolean {
        return getCorrespondingConversation(message) == null
    }

    private fun getCorrespondingConversation(message: IMessage): ConversationProgress? {
        return activeConversations.find { conversation ->
            conversation.participant == message.author &&
                    conversation.location == message.channel
        }
    }

    /**
     * Handles a conversation response.
     * @param message Message to handle, should be a response to any conversation.
     */
    fun handleConversationResponse(message: IMessage) {
        if (!isConversationResponse(message)) {
            return
        }

        val conversationProgress = getCorrespondingConversation(message)!!
        val nextPrompt = conversationProgress.progress.last().handleResponse(message)

        if (nextPrompt != null) {
            if (conversationProgress.progress.size + 1 > MAX_CONVERSATION_LENGTH) {
                bot.sendError(conversationProgress.location, "This conversation has exceeded its maximum length. Please try again.")
            }

            conversationProgress.progress += nextPrompt
        } else {
            conversationProgress.onCompleted(Conversation(conversationProgress.progress, conversationProgress.location,
                    conversationProgress.participant))
        }
    }
}