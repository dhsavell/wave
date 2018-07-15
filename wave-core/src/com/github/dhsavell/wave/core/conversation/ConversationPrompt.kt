package com.github.dhsavell.wave.core.conversation

import sx.blah.discord.handle.obj.IChannel
import sx.blah.discord.handle.obj.IMessage
import sx.blah.discord.handle.obj.IUser

sealed class PromptResult
data class NextPrompt(val nextPrompt: ConversationPrompt, val responseValue: String) : PromptResult()
object RepeatPrompt : PromptResult()
object FinishConversation : PromptResult()

/**
 * Interface representing a single prompt in a conversation.
 */
interface ConversationPrompt {
    /**
     * Name of this prompt, used for retrieving answers later.
     */
    val promptName: String

    /**
     * Prompts the conversation participant before awaiting their response.
     * @param destination Channel to send the message in.
     * @param participant User participating in the conversation.
     * @return Message sent as a prompt.
     */
    fun sendPrompt(destination: IChannel, participant: IUser): IMessage

    /**
     * Handles a response to the current conversation prompt.
     * @param response User's response.
     * @return The next ConversationPrompt to prompt the user with, or null if the conversation is over.
     */
    fun handleResponse(response: IMessage, pastResponses: Map<String, String>): PromptResult
}