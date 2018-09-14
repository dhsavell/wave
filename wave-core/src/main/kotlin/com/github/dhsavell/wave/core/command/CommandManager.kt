package com.github.dhsavell.wave.core.command

/**
 * A class for containing and managing commands.
 * @param commands Available commands.
 */
class CommandManager(val commands: List<Command>) {

    /**
     * Gets a command from a given call.
     *
     * In this case a "call" represents a message containing a command name and it's arguments. For example, "echo
     * Hello World!" is a call where "echo" is the command name and "Hello World!" is the argument.
     *
     * @param commandCall Full call to the command with no prefix.
     */
    fun getCommandFromCall(commandCall: String): Command? {
        val callComponents = commandCall.split(" ")
        val commandName = callComponents[0].toLowerCase()
        return commands.firstOrNull { command ->
            command.name == commandName || command.aliases.contains(commandName)
        }
    }
}