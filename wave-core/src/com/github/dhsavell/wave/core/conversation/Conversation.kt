package com.github.dhsavell.wave.core.conversation

import sx.blah.discord.handle.obj.IChannel
import sx.blah.discord.handle.obj.IUser

class Conversation(private val conversationPrompts: List<ConversationPrompt>, private val location: IChannel, private val participant: IUser) {

    fun getResponse(questionName: String): String? {
        return conversationPrompts
    }
}