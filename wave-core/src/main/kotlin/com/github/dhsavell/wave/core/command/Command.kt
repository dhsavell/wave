package com.github.dhsavell.wave.core.command

import com.github.dhsavell.wave.core.bot.Bot
import com.github.dhsavell.wave.core.permission.Permission
import com.github.dhsavell.wave.core.permission.ServerAdminsCanUse
import sx.blah.discord.handle.obj.IMessage

sealed class CommandResult
object CommandFailed : CommandResult()
object CommandSucceeded : CommandResult()
data class CommandSucceededWithValue(val message: String) : CommandResult()

/**
 * Interface representing a command that can be called by a user.
 */
interface Command {
    /**
     * Name of this command, used both for display and call detection.
     */
    val name: String

    /**
     * Category of this command.
     */
    val category: Category

    /**
     * Default command of this permission. Can be overridden in servers.
     */
    val defaultPermission: Permission
        get() = ServerAdminsCanUse

    /**
     * Aliases of this command.
     */
    val aliases: Array<String>
        get() = emptyArray()

    /**
     * Invokes this command.
     * @param message Message containing the command invocation.
     * @return Whether or not the command was executed successfully.
     */
    operator fun invoke(bot: Bot, message: IMessage, args: List<String>): CommandResult
}