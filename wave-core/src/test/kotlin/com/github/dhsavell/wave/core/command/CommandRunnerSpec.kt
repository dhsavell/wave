package com.github.dhsavell.wave.core.command

import com.github.dhsavell.wave.core.command.impl.AdministrativeCommand
import com.github.dhsavell.wave.core.command.impl.HelloWorldCommand
import com.github.dhsavell.wave.core.command.impl.NumberRepeaterCommand
import com.github.dhsavell.wave.core.permission.AllUsers
import com.github.dhsavell.wave.core.permission.GuildPermissionContainer
import com.github.dhsavell.wave.core.permission.InsufficientPermissionException
import com.github.dhsavell.wave.core.testutil.failWith
import com.github.dhsavell.wave.core.testutil.guild
import com.github.dhsavell.wave.core.testutil.message
import com.github.dhsavell.wave.core.testutil.succeedWith
import com.github.dhsavell.wave.core.testutil.user
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec
import org.dizitart.kno2.nitrite

class CommandRunnerSpec : WordSpec({
    val testCommandRunner = CommandRunner(
        listOf(
            NumberRepeaterCommand.Factory,
            HelloWorldCommand.Factory,
            AdministrativeCommand.Factory
        )
    )

    val db = nitrite { }

    "A command runner" should {
        "be able to call commands by name" {
            testCommandRunner.runCommand(
                "hello --name Bob",
                message(),
                db,
                skipPermissionCheck = true
            ) should succeedWith("Hello Bob!")

            testCommandRunner.runCommand(
                "number --number 2",
                message(),
                db,
                skipPermissionCheck = true
            ) should succeedWith("number is 2")
        }

        "return a CommandNotFound result upon receiving an invalid command" {
            testCommandRunner.runCommand("asdf", message(), db, skipPermissionCheck = true) shouldBe CommandNotFound
        }

        "wrap exceptions thrown from invalid syntax" {
            testCommandRunner.runCommand(
                "hello --invalid-option sdfsgda",
                message(), db, skipPermissionCheck = true
            ) should failWith<InvalidCommandSyntaxException>()
        }

        "check permissions for CommandFactories that support it" {
            val serverOwner = user(id = 12345)
            val otherUser = user(id = 67890)

            val guild = guild(owner = serverOwner)

            testCommandRunner.runCommand(
                "admin",
                message(author = serverOwner, guild = guild),
                db
            ) should succeedWith("success")

            testCommandRunner.runCommand(
                "admin",
                message(author = otherUser, guild = guild),
                db
            ) should failWith<InsufficientPermissionException>()
        }

        "respect permission overrides when checking permissions" {
            val serverOwner = user(id = 12345)
            val otherUser = user(id = 67890)

            val guild = guild(owner = serverOwner)

            GuildPermissionContainer(guild, db).updatePermissionOverrideFor("admin", AllUsers)

            testCommandRunner.runCommand(
                "admin",
                message(author = otherUser, guild = guild),
                db
            ) should succeedWith("success")
        }
    }
})