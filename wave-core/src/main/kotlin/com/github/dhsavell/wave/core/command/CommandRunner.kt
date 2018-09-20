package com.github.dhsavell.wave.core.command

import com.github.dhsavell.wave.core.command.factory.CommandFactory
import sx.blah.discord.handle.obj.IMessage

/**
 * A container of CommandFactories responsible for calling defined commands by name.
 * @param commandFactories List of available CommandFactories
 */
class CommandRunner(val commandFactories: List<CommandFactory>) {

    /**
     * Attempts to run a command given a call (with no additional prefix) and message context.
     * @param callWithoutPrefix Command call to run
     * @param context Message providing context about the executing environment
     */
    fun runCommand(callWithoutPrefix: String, context: IMessage): CommandResult {
        val commandName = callWithoutPrefix.split(" ")[0]
        val correspondingFactory = commandFactories.find { factory -> factory.canProvideFor(commandName, context) }

        return correspondingFactory?.getAction(context)?.execute() ?: CommandNotFound
    }
}