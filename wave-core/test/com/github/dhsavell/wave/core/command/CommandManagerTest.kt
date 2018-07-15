package com.github.dhsavell.wave.core.command

import com.github.dhsavell.wave.core.bot.ModularBot
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import sx.blah.discord.handle.obj.IMessage

class SomeCommand : Command {
    override val name: String = "somecommand"
    override val aliases: Array<String> = arrayOf("sc")

    override fun isUsable(bot: ModularBot, message: IMessage): Boolean {
        return true
    }

    override fun call(bot: ModularBot, message: IMessage, args: Array<String>): CommandResult {
        return SuccededUnit
    }
}

internal class CommandManagerTest {
    private val someCommand = SomeCommand()
    private val commandManager = CommandManager(listOf(someCommand))

    @Test
    fun testNonexistentCommandCall() {
        assertNull(commandManager.getCommandFromCall("nonexistent command call")) {
            return@assertNull "A call to a nonexistent command should return null"
        }
    }

    @Test
    fun testCommandCallByName() {
        assertEquals(someCommand, commandManager.getCommandFromCall("somecommand and then some args")) {
            return@assertEquals "A call to a command by its name should return that command"
        }
    }

    @Test
    fun testCommandCallByAlias() {
        assertEquals(someCommand, commandManager.getCommandFromCall("sc and then some args")) {
            return@assertEquals "A call to a command by its alias should return that command"
        }
    }

    @Test
    fun testCommandArgumentsEmpty() {
        assertArrayEquals(arrayOf<String>(), commandManager.getArgumentsFromCall("sc")) {
            return@assertArrayEquals "A call with no arguments should return an empty array"
        }
    }

    @Test
    fun testCommandArgumentExtraction() {
        assertArrayEquals(arrayOf("arg1", "arg2"), commandManager.getArgumentsFromCall("sc arg1 arg2")) {
            return@assertArrayEquals "Arguments are successfully extracted from a command call"
        }
    }
}