package com.github.dhsavell.wave.core.command

import sx.blah.discord.handle.obj.IMessage

/**
 * A simple CommandFactory implementation for matching commands by name.
 */
open class SimpleCommandFactory(
    private val primaryCommandName: String,
    private val commandAliases: List<String> = emptyList(),
    private val commandProvider: (String, IMessage) -> Command
) : CommandFactory {
    override fun canProvideFor(commandName: String, context: IMessage): Boolean {
        return primaryCommandName.equals(commandName, ignoreCase = true) ||
            (commandAliases.isNotEmpty() &&
                commandAliases.map { alias -> alias.toLowerCase() }.contains(commandName.toLowerCase()))
    }

    override fun getAction(commandCall: String, context: IMessage): Command = commandProvider(commandCall, context)
}