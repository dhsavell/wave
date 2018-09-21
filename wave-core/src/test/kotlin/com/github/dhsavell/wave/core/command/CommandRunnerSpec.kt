package com.github.dhsavell.wave.core.command

import com.github.dhsavell.wave.core.command.impl.HelloWorldCommand
import com.github.dhsavell.wave.core.command.impl.NumberRepeaterCommand
import com.github.dhsavell.wave.core.testutil.failWith
import com.github.dhsavell.wave.core.testutil.message
import com.github.dhsavell.wave.core.testutil.succeedWith
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec
import org.dizitart.kno2.nitrite

class CommandRunnerSpec : WordSpec({
    val testCommandRunner = CommandRunner(
        listOf(
            NumberRepeaterCommand.Factory,
            HelloWorldCommand.Factory
        )
    )

    val db = nitrite { }

    "A command runner" should {
        "be able to call commands by name" {
            testCommandRunner.runCommand("hello --name Bob", message(), db) should succeedWith("Hello Bob!")

            testCommandRunner.runCommand("number --number 2", message(), db) should succeedWith("number is 2")
        }

        "return a CommandNotFound result upon receiving an invalid command" {
            testCommandRunner.runCommand("asdf", message(), db) shouldBe CommandNotFound
        }

        "wrap exceptions thrown from invalid syntax" {
            testCommandRunner.runCommand(
                "hello --invalid-option sdfsgda",
                message(), db
            ) should failWith<InvalidCommandSyntaxException>()
        }
    }
})