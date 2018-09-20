package com.github.dhsavell.wave.core.command.factory

import com.github.dhsavell.wave.core.command.Command
import sx.blah.discord.handle.obj.IMessage

/**
 * Represents a provider of commands. When checking chats for command calls, this is the first object dealt with: if
 * a match occurs, then the appropriate Command is instantiated an run.
 */
interface CommandFactory {
    fun canProvideFor(commandName: String, context: IMessage): Boolean
    fun getAction(commandCall: String, context: IMessage): Command
}