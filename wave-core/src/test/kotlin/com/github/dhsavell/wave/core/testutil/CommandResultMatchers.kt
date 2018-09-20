package com.github.dhsavell.wave.core.testutil

import com.github.dhsavell.wave.core.command.CommandFailedWithException
import com.github.dhsavell.wave.core.command.CommandResult
import com.github.dhsavell.wave.core.command.CommandSucceededWithValue
import io.kotlintest.Matcher
import io.kotlintest.Result

fun succeedWith(expectedValue: String): Matcher<CommandResult> = object : Matcher<CommandResult> {
    override fun test(value: CommandResult): Result {
        return when {
            value !is CommandSucceededWithValue -> Result(false, "Command did not succeed", "Command succeeded")
            value.result != expectedValue -> Result(
                false,
                "Command succeeded with ${value.result}, but $expectedValue was expected",
                "Command succeeded with the correct result"
            )
            else -> Result(
                true,
                "Command succeeded with the expected result",
                "Command did not succeed with the expectede result"
            )
        }
    }
}

inline fun <reified T> failWith(): Matcher<CommandResult> = object : Matcher<CommandResult> {
    override fun test(value: CommandResult): Result {
        return when {
            value !is CommandFailedWithException -> Result(
                false,
                "Command did not fail",
                "Command failed with the expected type"
            )
            value.exception !is T -> Result(
                false,
                "Command failed with ${value.exception::class}, but a(n) ${T::class} was expected",
                "Command failed with the expected type"
            )
            else -> Result(
                true,
                "Command failed with the expected type",
                "Command did not fail with the expected type"
            )
        }
    }
}