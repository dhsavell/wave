package com.github.dhsavell.wave.app.command.settings

import com.github.dhsavell.wave.app.command.Settings
import com.github.dhsavell.wave.app.provider.CommandProvider
import com.github.dhsavell.wave.core.bot.Bot
import com.github.dhsavell.wave.core.bot.sendSuccess
import com.github.dhsavell.wave.core.command.*
import com.github.dhsavell.wave.core.permission.Permission
import com.github.dhsavell.wave.core.permission.ServerAdminsCanUse
import org.mapdb.DB
import picocli.CommandLine
import sx.blah.discord.handle.obj.IMessage

@CommandProvider
class CommandSetPermission : ArgParserCommand<CommandSetPermission.Args> {
    override val name: String = "permission"
    override val category: Category = Settings
    override val defaultPermission: Permission = ServerAdminsCanUse
    override fun get(): Args = Args()

    override fun invokeWithArgs(bot: Bot, db: DB, message: IMessage, args: Args): CommandResult {
        args.commands.forEach { command ->
            bot.permissionManager.setOverride(command, args.permission, message.guild)
        }

        message.channel.sendSuccess("Updated required permission for **${args.commands.size}** command(s) to " +
                "**${args.permission.name}**.")

        return CommandSucceeded
    }

    @CommandLine.Command(name = "permission", description = ["sets the permission level of a command"])
    class Args {
        @CommandLine.Parameters(description = ["command(s) to set permission level of"])
        var commands: Array<Command> = emptyArray()

        @CommandLine.Option(names = ["--permission", "-p"], required = true, description = ["permission level to use"])
        lateinit var permission: Permission
    }
}