package com.github.dhsavell.wave.core.command

import com.github.dhsavell.wave.core.bot.Bot
import com.github.dhsavell.wave.core.bot.BotColors
import com.github.dhsavell.wave.core.bot.sendEmbed
import com.github.dhsavell.wave.core.permission.Permission
import com.github.dhsavell.wave.core.util.toChannelFromIdentifier
import com.github.dhsavell.wave.core.util.toRoleFromIdentifier
import com.github.dhsavell.wave.core.util.toUserFromIdentifier
import org.mapdb.DB
import picocli.CommandLine
import sx.blah.discord.handle.obj.IChannel
import sx.blah.discord.handle.obj.IMessage
import sx.blah.discord.handle.obj.IRole
import sx.blah.discord.handle.obj.IUser
import java.util.function.Supplier

interface ArgParserCommand<T> : Command, Supplier<T> {
    fun invokeWithArgs(bot: Bot, db: DB, message: IMessage, args: T): CommandResult
    override fun invoke(bot: Bot, db: DB, message: IMessage, args: List<String>): CommandResult {
        return try {
            val parser = CommandLine(get())
            val guild = message.guild

            parser.registerConverter(IUser::class.java) { string -> string.toUserFromIdentifier(guild) }
            parser.registerConverter(IChannel::class.java) { string -> string.toChannelFromIdentifier(guild) }
            parser.registerConverter(IRole::class.java) { string -> string.toRoleFromIdentifier(guild) }
            parser.registerConverter(Command::class.java, bot.commandManager::getCommandFromCall)
            parser.registerConverter(Permission::class.java) { string ->
                Permission.fromString(string, guild)
                        ?: throw CommandLine.ParameterException(parser, "Invalid permission.")
            }

            val argsObject = parser.parse(*args.toTypedArray())[0].getCommand<T>()
            invokeWithArgs(bot, db, message, argsObject)
        } catch (e: CommandLine.ParameterException) {
            if (message.channel != null) {
                message.channel.sendEmbed {
                    title { "Invalid arguments for `$name`" }
                    color { BotColors.ERROR }
                    description { e.message ?: "```${e.commandLine.usageMessage}```" }
                }
            }

            CommandFailed
        }
    }
}