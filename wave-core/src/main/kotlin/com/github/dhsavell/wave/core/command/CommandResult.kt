package com.github.dhsavell.wave.core.command

sealed class CommandResult(val description: String)

object CommandSucceded : CommandResult("The command was completed successfully.")
object CommandFailed : CommandResult("The command failed to complete successfully.")
object CommandNotFound : CommandResult("The specified command was not found.")
class CommandSucceededWithValue(val value: String) :
    CommandResult("The command was completed successfully and yielded \"$value\"")