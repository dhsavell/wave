package com.github.dhsavell.wave.core.util

import sx.blah.discord.handle.obj.IChannel
import sx.blah.discord.handle.obj.IMessage

fun IChannel.sendEmbed(initBlock: DslEmbedBuilder.() -> Unit): IMessage {
    return sendMessage(embed(initBlock))
}

fun IChannel.sendInfo(message: String): IMessage = sendEmbed {
    title { message }
    color { StatusColors.INFO }
}

fun IChannel.sendWarning(message: String): IMessage = sendEmbed {
    title { "**Warning**: $message" }
    color { StatusColors.WARNING }
}

fun IChannel.sendError(message: String): IMessage = sendEmbed {
    title { "**Error**: $message" }
    color { StatusColors.ERROR }
}

fun IChannel.sendSuccess(message: String): IMessage = sendEmbed {
    title { "**Success**: $message" }
    color { StatusColors.SUCCESS }
}