package com.github.dhsavell.wave.core.testutil

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import sx.blah.discord.handle.obj.IMessage

fun messageWithContent(messageContent: String): IMessage {
    return mock {
        on { content } doReturn messageContent
    }
}

fun stubMessage(): IMessage {
    return mock()
}