package com.github.dhsavell.wave.core.command

sealed class CommandResult

object CommandSucceded : CommandResult()
object CommandFailed : CommandResult()

class CommandSuccededWithValue(val value: String) : CommandResult()