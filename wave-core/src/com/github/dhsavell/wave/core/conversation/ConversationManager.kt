package com.github.dhsavell.wave.core.conversation

import sx.blah.discord.handle.obj.IChannel
import sx.blah.discord.handle.obj.IMessage
import sx.blah.discord.handle.obj.IUser

data class ActiveConversation(var progress: List<ConversationPrompt>,
                              var responses: Map<String, String>,
                              val location: IChannel,
                              val participant: IUser)

/**
 * A class for managing multiple ongoing Conversations at once.
 */
class ConversationManager {
    private var activeConversations: List<ActiveConversation> = listOf()

    /**
     * Starts a conversation with a given prompt.
     * @param start First conversation prompt.
     * @param location Channel the conversation is taking place in.
     * @param participant User participating in the conversation.
     */
    fun beginConversation(start: ConversationPrompt, location: IChannel, participant: IUser) {
        start.sendPrompt(location, participant)
        activeConversations += ActiveConversation(listOf(start), mapOf(), location, participant)
    }

    /**
     * Determines whether or not a message is a conversation response.
     */
    fun isResponse(message: IMessage): Boolean {
        return getCorrespondingConversation(message) == null
    }

    /**
     * Attempts to get the corresponding ActiveConversation from a given message.
     */
    private fun getCorrespondingConversation(message: IMessage): ActiveConversation? {
        return activeConversations.find { conversation ->
            conversation.participant == message.author &&
                    conversation.location == message.channel
        }
    }

    private fun removeConversation(activeConversation: ActiveConversation) {
        activeConversations = activeConversations.filterNot { it == activeConversation }
    }

    /**
     * Handles a conversation response.
     *
     * This could be: sending the next prompt to the user, repeating the prompt (in the event of invalid input), or
     * ending the conversation when it is finished.
     *
     * @param message Message to handle, should be a response to any active conversation.
     */
    fun handleResponse(message: IMessage) {
        val activeConversation = getCorrespondingConversation(message) ?: return

        if (message.content.equals("cancel", true)) {
            return
        }

        val activePrompt = activeConversation.progress.last()
        val promptResult = activePrompt.handleResponse(message, activeConversation.responses)

        when (promptResult) {
            is NextPrompt -> {
                promptResult.nextPrompt.sendPrompt(activeConversation.location, activeConversation.participant)
                activeConversation.responses += Pair(activePrompt.promptName, promptResult.responseValue)
                activeConversation.progress += promptResult.nextPrompt
            }
            is RepeatPrompt -> {
                activePrompt.sendPrompt(activeConversation.location, activeConversation.participant)
            }
            is FinishConversation -> {
                removeConversation(activeConversation)
            }
        }
    }
}