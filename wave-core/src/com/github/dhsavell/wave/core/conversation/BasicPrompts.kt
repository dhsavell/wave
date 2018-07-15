package com.github.dhsavell.wave.core.conversation

import sx.blah.discord.api.internal.json.objects.EmbedObject
import sx.blah.discord.handle.obj.IChannel
import sx.blah.discord.handle.obj.IMessage
import sx.blah.discord.handle.obj.IUser
import sx.blah.discord.util.EmbedBuilder

private fun buildEmbed(prompt: String, participant: IUser, type: String = "Any text"): EmbedObject {
    return EmbedBuilder()
            .withTitle(prompt)
            .withDesc(participant.mention() + ", your next message in this channel will be used as a response.")
            .appendField("Respond with:", type, true)
            .build()
}

abstract class AbstractPrompt(override val promptName: String, private val expectedType: String,
                              private val nextPrompt: ConversationPrompt? = null) : ConversationPrompt {
    override fun sendPrompt(destination: IChannel, participant: IUser): IMessage {
        return destination.sendMessage(buildEmbed(promptName, participant, expectedType))
    }

    override fun handleResponse(response: IMessage, pastResponses: Map<String, String>): PromptResult {
        return if (validateInput(response)) {
            return if (nextPrompt != null) {
                NextPrompt(nextPrompt, getContentFromResponse(response))
            } else {
                FinishConversation
            }
        } else {
            RepeatPrompt
        }
    }

    open fun getContentFromResponse(response: IMessage): String {
        return response.content
    }

    abstract fun validateInput(response: IMessage): Boolean
}

class StringPrompt(override val promptName: String, expectedType: String, nextPrompt: ConversationPrompt? = null) :
        AbstractPrompt(promptName, expectedType, nextPrompt) {
    override fun validateInput(response: IMessage): Boolean {
        return true
    }
}

class ChannelPrompt(override val promptName: String, expectedType: String, nextPrompt: ConversationPrompt? = null) :
        AbstractPrompt(promptName, expectedType, nextPrompt) {
    override fun validateInput(response: IMessage): Boolean {
        return response.channelMentions.isNotEmpty()
    }

    override fun getContentFromResponse(response: IMessage): String {
        return response.channelMentions[0].stringID
    }
}