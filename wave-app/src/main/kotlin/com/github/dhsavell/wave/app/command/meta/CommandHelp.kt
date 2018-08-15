package com.github.dhsavell.wave.app.command.meta

import com.github.dhsavell.wave.app.command.Meta
import com.github.dhsavell.wave.app.provider.CommandProvider
import com.github.dhsavell.wave.core.bot.Bot
import com.github.dhsavell.wave.core.bot.sendEmbed
import com.github.dhsavell.wave.core.command.Category
import com.github.dhsavell.wave.core.command.Command
import com.github.dhsavell.wave.core.command.CommandResult
import com.github.dhsavell.wave.core.command.CommandSucceeded
import sx.blah.discord.handle.obj.IMessage

@CommandProvider
class CommandHelp : Command {
    override val name: String = "help"
    override val category: Category = Meta

    override fun invoke(bot: Bot, message: IMessage, args: List<String>): CommandResult {
        message.channel.sendEmbed {
            title { "Help" }
            description {
                "To get help for a command, use the following syntax: `<command> --help`\n" +
                        "For a list of commands, see the online documentation."
            }
        }

        return CommandSucceeded
    }

}