package com.github.dhsavell.wave.app.command

import com.github.dhsavell.wave.app.provider.BotCommand
import com.github.dhsavell.wave.core.bot.Bot
import com.github.dhsavell.wave.core.command.Command
import com.github.dhsavell.wave.core.command.CommandResult
import com.github.dhsavell.wave.core.command.CommandSucceededWithValue
import sx.blah.discord.handle.obj.IMessage

@BotCommand
class CommandPing : Command {
    override val name: String = "ping"

    override fun call(bot: Bot, message: IMessage, args: Array<String>): CommandResult {
        val response = "Hello, ${message.author.mention()}!"
        bot.sendInfo(message.channel, response)
        return CommandSucceededWithValue(response)
    }
}