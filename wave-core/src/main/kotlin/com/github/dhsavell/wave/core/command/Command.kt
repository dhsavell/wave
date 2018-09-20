package com.github.dhsavell.wave.core.command

/**
 * Represents a command action. A new Command is instantiated for each command call.
 *
 * Commands can and should throw exceptions upon failure. Ideally, the executing environment will wrap them into a
 * CommandFailedWithException result.
 */
interface Command {

    /**
     * Executes this command and returns a result. This result is never displayed to the user directly (unless an
     * error occurs), and is instead used for piping the output of one command into another.
     */
    fun execute(): CommandResult
}