package com.github.dhsavell.wave.core.command

import com.github.dhsavell.wave.core.bot.Bot
import com.github.dhsavell.wave.core.bot.BotColors
import com.github.dhsavell.wave.core.bot.sendEmbed
import com.github.dhsavell.wave.core.bot.sendError
import com.xenomachina.argparser.*
import org.mapdb.DB
import sx.blah.discord.handle.obj.IChannel
import sx.blah.discord.handle.obj.IMessage
import java.io.StringWriter

abstract class ArgParserCommand<T>(override val name: String, override val category: Category) : Command {
    abstract fun createArgsObject(parser: ArgParser, bot: Bot, context: IMessage): T
    abstract fun invoke(bot: Bot, db: DB, message: IMessage, args: T): CommandResult

    override fun invoke(bot: Bot, db: DB, message: IMessage, args: List<String>): CommandResult {
        return try {
            val parsedArgs = ArgParser(args.toTypedArray()).parseInto { createArgsObject(it, bot, message) }
            invoke(bot, db, message, parsedArgs)
        } catch (e: Exception) {
            when (e) {
                is ShowHelpException -> sendHelpEmbed(message.channel, e.getMessageText(name))
                is SystemExitException -> message.channel.sendError(e.getMessageText(name))
                else -> message.channel.sendError("An error occurred while trying to run that command.")
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

fun SystemExitException.getMessageText(name: String, columns: Int = 55): String {
    return when (this) {
        is UnrecognizedOptionException -> "Unknown option `$optName`."
        is MissingValueException -> "No value specified for `$valueName`."
        is OptionMissingRequiredArgumentException -> "The option `$optName` is missing a required value."
        is MissingRequiredPositionalArgumentException -> "An argument for `$argName` is missing."
        is UnexpectedOptionArgumentException -> "The option `$optName` doesn't need an argument."
        is UnexpectedPositionalArgumentException -> "The argument `$valueName` is misplaced or invalid."
        else -> {
            val writer = StringWriter()
            printUserMessage(writer, name, columns)
            writer.toString()
        }
    }
}

