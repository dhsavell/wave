package com.github.dhsavell.wave.core.command

import com.github.dhsavell.wave.core.bot.ModularBot
import sx.blah.discord.handle.obj.IMessage

sealed class CommandResult
object Failed : CommandResult()
object SuccededUnit : CommandResult()
data class SuccededWithValue(val message: String) : CommandResult()

/**
 * Interface representing a command that can be called by a user.
 */
interface Command {
    /**
     * Name of this command, used both for display and call detection.
     */
    val name: String

    /**
     * Aliases of this command.
     */
    val aliases: Array<String>

    /**
     * Determines whether or not a command is usable by a user.
     * @param bot Bot calling this command.
     * @param message Message containing the command invocation.
     * @return Whether or not the user can use this command.
     */
    fun isUsable(bot: ModularBot, message: IMessage): Boolean

    /**
     * Executes this command.
     * @param bot Bot calling this command.
     * @param message Message containing the command invocation.
     * @return Whether or not the command was executed successfully.
     */
    fun call(bot: ModularBot, message: IMessage, args: Array<String>): CommandResult
}