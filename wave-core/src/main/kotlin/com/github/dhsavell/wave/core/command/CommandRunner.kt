package com.github.dhsavell.wave.core.command

import com.xenomachina.argparser.SystemExitException
import org.dizitart.no2.Nitrite
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
    fun runCommand(callWithoutPrefix: String, context: IMessage, db: Nitrite): CommandResult {
        val commandName = callWithoutPrefix.split(" ")[0]
        val correspondingFactory = commandFactories.find { factory -> factory.canProvideFor(commandName, context, db) }

        return try {
            correspondingFactory?.getAction(callWithoutPrefix, context, db)?.execute() ?: CommandNotFound
        } catch (e: SystemExitException) {
            CommandFailedWithException(InvalidCommandSyntaxException(e.message ?: "invalid command syntax", e))
        }
    }
}