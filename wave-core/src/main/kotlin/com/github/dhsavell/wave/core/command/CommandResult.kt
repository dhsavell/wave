package com.github.dhsavell.wave.core.command

/**
 * Represents the result of a command.
 */
sealed class CommandResult(val description: String, val wasSuccessful: Boolean = true)

/**
 * Signifies success with no resulting value.
 */
object CommandSucceded : CommandResult("The command was completed successfully.")

/**
 * Signifies a generic failure.
 */
object CommandFailed : CommandResult("The command failed to complete successfully.", wasSuccessful = false)

/**
 * Signifies an attempt to call a nonexistent command.
 */
object CommandNotFound : CommandResult("The specified command was not found.", wasSuccessful = false)

/**
 * Signifies success with a value, which must be a String. This is used for piping commands together.
 */
class CommandSucceededWithValue(val result: String) :
    CommandResult("The command was completed successfully and yielded \"$result\"")

/**
 * Signifies a failure caused by a specific exception.
 */
class CommandFailedWithException(val exception: Exception) :
    CommandResult("An error occurred: ${exception.message} (${exception::class})", wasSuccessful = false)