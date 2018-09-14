package com.github.dhsavell.wave.core.conversation.builder

import com.github.dhsavell.wave.core.conversation.FinishConversation
import com.github.dhsavell.wave.core.conversation.NextPrompt
import com.github.dhsavell.wave.core.conversation.Prompt
import com.github.dhsavell.wave.core.conversation.PromptResult
import com.github.dhsavell.wave.core.conversation.RepeatPrompt
import com.github.dhsavell.wave.core.util.embed
import sx.blah.discord.handle.obj.IChannel
import sx.blah.discord.handle.obj.IMessage
import sx.blah.discord.handle.obj.IUser

class ConversationPrompt(
    override val promptName: String,
    private val question: String,
    private val handler: (IMessage, Map<String, String>) -> Unit,
    private val validator: (IMessage, Map<String, String>) -> Boolean = { _, _ -> true }
) : Prompt {

    lateinit var nextPrompt: Prompt

    override fun sendPrompt(destination: IChannel, participant: IUser): IMessage {
        return destination.sendMessage(embed {
            title { question }
            footer { "${participant.mention()}, your next message in this channel will be used as a response." }
        })
    }

    override fun handleResponse(response: IMessage, pastResponses: Map<String, String>): PromptResult {
        return if (validator(response, pastResponses)) {
            handler(response, pastResponses)

            if (::nextPrompt.isInitialized) {
                NextPrompt(nextPrompt, response.content)
            } else {
                FinishConversation
            }
        } else {
            RepeatPrompt
        }
    }
}