package com.github.dhsavell.wave.core.command

import sx.blah.discord.handle.obj.IMessage

/**
 * Represents a provider of commands. When checking chats for command calls, this is the first object dealt with: if
 * a match occurs, then the appropriate Command is instantiated an run.
 */
interface CommandFactory {

    /**
     * Determines whether or not this CommandFactory can provide a command for the given command name.
     */
    fun canProvideFor(commandName: String, context: IMessage): Boolean

    /**
     * Gets the appropriate Command for a given call and context. In most cases, canProvideFor will already be used
     * to verify the call.
     */
    fun getAction(commandCall: String, context: IMessage): Command
}