package com.github.dhsavell.wave.core.conversation

import com.github.dhsavell.wave.core.conversation.builder.conversation
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.kotlintest.matchers.beOfType
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import sx.blah.discord.handle.obj.IMessage

class ConversationBuilderTest : StringSpec({
    "Conversation prompts can be chained" {
        val firstPrompt = conversation {
            ask("What is your name?") {
                storeResultAs { "name" }
            }

            ask("What is your quest?") {
                storeResultAs { "quest" }
            }
        }

        val mockMessage = mock<IMessage> {
            on { content } doReturn "dummyContent"
        }

        val firstResult = firstPrompt.handleResponse(mockMessage, mapOf())
        firstResult should beOfType<NextPrompt>()

        val endResult = (firstResult as NextPrompt).nextPrompt.handleResponse(mockMessage, emptyMap())
        endResult should beOfType<FinishConversation>()
    }

    "Conversation prompts with validation request to be repeated upon failure" {
        val prompt = conversation {
            ask("This prompt will fail unless you enter a lowercase `a`.") {
                storeResultAs { "letter" }
                withValidation { message, _ ->
                    message.content.equals("a", true)
                }
            }
        }

        val failResult = prompt.handleResponse(mock { on { content } doReturn "not a" }, emptyMap())
        failResult should beOfType<RepeatPrompt>()

        val successResult = prompt.handleResponse(mock { on { content } doReturn "a" }, emptyMap())
        successResult should beOfType<FinishConversation>()
    }

    "Conversation prompts can execute an action when responded to" {
        var testString = "not changed"
        val prompt = conversation {
            ask("After being responded to, this prompt will set testString to the response dummyContent.") {
                storeResultAs { "dummyContent" }
                afterResponse { message, _ ->
                    testString = message.content
                }
            }
        }

        prompt.handleResponse(mock { on { content } doReturn "changed" }, emptyMap())
        testString shouldBe "changed"
    }
})