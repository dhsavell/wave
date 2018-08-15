package com.github.dhsavell.wave.core.command

import com.github.dhsavell.wave.core.bot.Bot
import com.github.dhsavell.wave.core.bot.BotColors
import com.github.dhsavell.wave.core.bot.sendEmbed
import com.github.dhsavell.wave.core.bot.sendError
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.ShowHelpException
import com.xenomachina.argparser.SystemExitException
import sx.blah.discord.handle.obj.IChannel
import sx.blah.discord.handle.obj.IMessage

abstract class ArgParserCommand<T>(override val name: String, override val category: Category) : Command {
    abstract fun createArgsObject(parser: ArgParser, bot: Bot, context: IMessage): T
    abstract fun invoke(bot: Bot, message: IMessage, args: T): CommandResult

    override fun invoke(bot: Bot, message: IMessage, args: List<String>): CommandResult {
        return try {
            val parsedArgs = ArgParser(args.toTypedArray()).parseInto { createArgsObject(it, bot, message) }
            invoke(bot, message, parsedArgs)
        } catch (e: Exception) {
            when (e) {
                is ShowHelpException -> message.channel?.run { sendHelpEmbed(this, e.getMessageText(name)) }
                is SystemExitException -> message.channel?.sendError(e.getMessageText(name))
                else -> message.channel?.sendError(e.message ?:
                    "An unknown error occurred while trying to run that command. This is a bug!")
            }

            CommandFailed
        }
    }

    private fun sendHelpEmbed(channel: IChannel, helpText: String) {
        channel.sendEmbed {
            color { BotColors.SUCCESS }
            title { "Help for `$name`" }

            // The repeated period suffix is a bit of a hack, but it allows our code block on Discord to render wide
            // enough to display the entire help message on all (tested) devices.
            description { "```$helpText" + "\n" + ".".repeat(60) + "```" }
        }
    }
}