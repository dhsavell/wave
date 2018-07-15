package com.github.dhsavell.wave.core.bot

import sx.blah.discord.api.IDiscordClient
import sx.blah.discord.handle.obj.IChannel
import sx.blah.discord.handle.obj.IMessage

class ModularBot(private val client: IDiscordClient, private val defaultPrefix: String) {
    fun sendError(destination: IChannel, message: String): IMessage {
        TODO("Not implemented!")
    }
}