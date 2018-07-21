package com.github.dhsavell.wave.app.command.meta

import com.github.dhsavell.wave.app.command.Meta
import com.github.dhsavell.wave.core.bot.Bot
import com.github.dhsavell.wave.core.bot.sendEmbed
import com.github.dhsavell.wave.core.bot.sendError
import com.github.dhsavell.wave.core.bot.sendInfo
import com.github.dhsavell.wave.core.command.*
import com.github.dhsavell.wave.core.permission.AnybodyCanUse
import com.github.dhsavell.wave.core.permission.Permission
import org.mapdb.DB
import picocli.CommandLine
import sx.blah.discord.handle.obj.IMessage

class CommandHelp : ArgParserCommand<CommandHelp.Args> {
    override val name: String = "help"
    override val aliases: Array<String> = arrayOf("h")
    override val category: Category = Meta
    override val defaultPermission: Permission = AnybodyCanUse
    override fun get(): Args = Args()

    override fun invokeWithArgs(bot: Bot, db: DB, message: IMessage, args: Args): CommandResult {
        val command = bot.commandManager.getCommandFromCall(args.commandName)
        if (command == null) {
            message.channel.sendError("No commands named `${args.commandName}`.")
            return CommandFailed
        }

        when (command) {
            is ArgParserCommand<*> -> {
                val c = CommandLine(command.get())
                message.channel.sendEmbed {
                    title { command.name }
                    description { "Basic usage is provided below. For more information, see the online documentation." }
                    section("Usage", "```${c.usageMessage}```")
                }
            }

            else -> {
                message.channel.sendInfo("See the online documentation for information about this command.")
            }
        }

        return CommandSucceeded
    }

    @CommandLine.Command(description = ["displays a usage message for a command"])
    class Args {
        @CommandLine.Parameters(index = "0", description = ["command to get help for"])
        lateinit var commandName: String
    }
}