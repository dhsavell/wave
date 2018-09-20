package com.github.dhsavell.wave.core.command

import com.github.dhsavell.wave.core.command.impl.HelloWorldCommand
import com.github.dhsavell.wave.core.command.impl.NumberRepeaterCommand
import com.github.dhsavell.wave.core.testutil.failWith
import com.github.dhsavell.wave.core.testutil.stubMessage
import com.github.dhsavell.wave.core.testutil.succeedWith
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

class CommandRunnerSpec : WordSpec({
    val testCommandRunner = CommandRunner(
        listOf(
            NumberRepeaterCommand.Factory,
            HelloWorldCommand.Factory
        )
    )

    "A command runner" should {
        "be able to call commands by name" {
            testCommandRunner.runCommand("hello --name Bob", stubMessage()) should succeedWith("Hello Bob!")

            testCommandRunner.runCommand("number --number 2", stubMessage()) should succeedWith("number is 2")
        }

        "return a CommandNotFound result upon receiving an invalid command" {
            testCommandRunner.runCommand("asdf", stubMessage()) shouldBe CommandNotFound
        }

        "wrap exceptions thrown from invalid syntax" {
            testCommandRunner.runCommand(
                "hello --invalid-option sdfsgda",
                stubMessage()
            ) should failWith<InvalidCommandSyntaxException>()
        }
    }
})