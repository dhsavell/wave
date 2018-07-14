package com.github.dhsavell.wave.core.command

import com.github.dhsavell.wave.core.bot.ModularBot
import sx.blah.discord.handle.obj.IMessage

/**
 * Interface representing a command that can be called by a user.
 */
interface UserCommand {
    /**
     * Determines whether or not a command is usable by a user.
     * @param bot Bot this command belongs to.
     * @param message Message containing the command invocation.
     * @return Whether or not the user can use this command.
     */
    fun isUsable(bot: ModularBot, message: IMessage): Boolean

    /**
     * Calls this command.
     * @param bot Bot this command belongs to.
     * @param message Message containing the command invocation.
     * @return Whether or not the command was executed successfully.
     */
    fun call(bot: ModularBot, message: IMessage, args: String): Boolean
}