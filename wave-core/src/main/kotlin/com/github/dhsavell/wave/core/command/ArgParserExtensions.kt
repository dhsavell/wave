package com.github.dhsavell.wave.core.command

import com.xenomachina.argparser.*
import java.io.StringWriter

fun SystemExitException.getMessageText(name: String, columns: Int = 55): String {
    return when (this) {
        is UnrecognizedOptionException -> "Unknown option `$optName`."
        is MissingValueException -> "No value specified for `$valueName`."
        is OptionMissingRequiredArgumentException -> "The option `$optName` is missing a required value."
        is MissingRequiredPositionalArgumentException -> "An argument for `$argName` is missing."
        is UnexpectedOptionArgumentException -> "The option `$optName` doesn't need an argument."
        is UnexpectedPositionalArgumentException -> "The argument `$valueName` is misplaced or invalid."
        else -> {
            val writer = StringWriter()
            printUserMessage(writer, name, columns)
            writer.toString()
        }
    }
}
