package com.github.dhsavell.wave.core.conversation.builder

import com.github.dhsavell.wave.core.conversation.Prompt
import sx.blah.discord.handle.obj.IMessage

class ConversationBuilder internal constructor(conversationInit: ConversationBuilder.() -> Unit) {
    private var prompts: List<ConversationPrompt> = emptyList()

    init {
        conversationInit()
    }

    fun ask(question: String, promptInit: PromptBuilder.() -> Unit) {
        val result = PromptBuilder(question, promptInit).build()

        prompts.lastOrNull()?.nextPrompt = result
        prompts += result
    }

    fun build(): Prompt {
        return prompts.first()
    }

    class PromptBuilder(private val question: String, promptInit: PromptBuilder.() -> Unit) {
        private var name: String = "unnamed"
        private var handler: (IMessage, Map<String, String>) -> Unit = { _, _ -> Unit }
        private var validator: (IMessage, Map<String, String>) -> Boolean = { _, _ -> true }

        init {
            promptInit()
        }

        fun storeResultAs(name: () -> String) {
            this.name = name()
        }

        fun afterResponse(handler: (IMessage, Map<String, String>) -> Unit) {
            this.handler = handler
        }

        fun withValidation(validator: (IMessage, Map<String, String>) -> Boolean) {
            this.validator = validator
        }

        fun build(): ConversationPrompt {
            return ConversationPrompt(name, question, handler, validator)
        }
    }
}

fun conversation(conversationInit: ConversationBuilder.() -> Unit) = ConversationBuilder(conversationInit).build()