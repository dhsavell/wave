package com.github.dhsavell.wave.app.command.settings

import com.github.dhsavell.wave.app.command.Settings
import com.github.dhsavell.wave.app.provider.CommandProvider
import com.github.dhsavell.wave.core.bot.Bot
import com.github.dhsavell.wave.core.bot.sendSuccess
import com.github.dhsavell.wave.core.command.ArgParserCommand
import com.github.dhsavell.wave.core.command.CommandManager
import com.github.dhsavell.wave.core.command.CommandResult
import com.github.dhsavell.wave.core.command.CommandSucceeded
import com.github.dhsavell.wave.core.permission.Permission
import com.github.dhsavell.wave.core.permission.ServerAdminsCanUse
import com.xenomachina.argparser.ArgParser
import sx.blah.discord.handle.obj.IGuild
import sx.blah.discord.handle.obj.IMessage

@CommandProvider
class CommandSetPermission : ArgParserCommand<CommandSetPermission.Args>("permission", Settings) {
    override val defaultPermission: Permission = ServerAdminsCanUse

    override fun createArgsObject(parser: ArgParser, bot: Bot, context: IMessage): Args {
        return Args(parser, bot.commandManager, context.guild)
    }

    override fun invoke(bot: Bot, message: IMessage, args: Args): CommandResult {
        args.commands.forEach { command ->
            bot.permissionManager.setOverride(command, args.permission, message.guild)
        }

        message.channel.sendSuccess(
            "Updated required permission for **${args.commands.size}** command(s) to " +
                "**${args.permission.name}**."
        )

        return CommandSucceeded
    }

    class Args(parser: ArgParser, commandManager: CommandManager, guild: IGuild) {
        val commands by parser.positionalList("commands to update permission of") {
            commandManager.getCommandFromCall(this) ?: throw IllegalArgumentException("No such command `$this`")
        }

        val permission by parser.storing("--permission", "-p", help = "permission to use") {
            Permission.fromString(this, guild)
        }
    }
}