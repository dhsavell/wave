package com.github.dhsavell.wave.core.command

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import org.mapdb.DB
import picocli.CommandLine
import sx.blah.discord.handle.obj.IGuild
import sx.blah.discord.handle.obj.IMessage
import sx.blah.discord.handle.obj.IUser

class TestCommand(private val onInvoked: (Args) -> Unit) : ArgParserCommand<TestCommand.Args> {
    override fun get(): Args = Args()

    override fun invokeWithArgs(db: DB, message: IMessage, args: Args): CommandResult {
        onInvoked(args)
        return CommandSucceededWithValue(args.message)
    }

    override val name: String = "test"
    override val category: Category = Category("", "")

    class Args {
        @CommandLine.Parameters(index = "0")
        lateinit var message: String

        @CommandLine.Parameters(index = "1..*")
        lateinit var ints: Array<Int>

        @CommandLine.Option(names = ["-f"])
        var flag: Boolean = false

        @CommandLine.Option(names = ["-u"])
        lateinit var user: IUser
    }
}

class ArgParserCommandTest : StringSpec({
    "Parameters can be passed to an ArgParserCommand" {
        val command = TestCommand { args ->
            args.message shouldBe "test"
            args.ints shouldBe arrayOf(1, 2, 3)
            args.flag shouldBe false
        }

        command(mock(), mock(), listOf("test", "1", "2", "3"))
    }

    "Discord4J types can be parsed" {
        val mockUser1 = mock<IUser> {
            on { longID } doReturn 100000000000000000
            on { name } doReturn "foo"
        }

        val mockUser2 = mock<IUser> {
            on { longID } doReturn 200000000000000000
            on { name } doReturn "bar"
        }

        val mockGuild = mock<IGuild> {
            on { users } doReturn listOf(mockUser1, mockUser2)
            on { getUserByID(100000000000000000) } doReturn mockUser1
            on { getUserByID(200000000000000000) } doReturn mockUser2
        }

        val mockMessage = mock<IMessage> {
            on { guild } doReturn mockGuild
        }

        val command = TestCommand { args ->
            args.user shouldBe mockUser2
        }

        command(mock(), mockMessage, listOf("test", "1", "2", "3", "-u", "bar"))
        command(mock(), mockMessage, listOf("test", "1", "2", "3", "-u", "200000000000000000"))
        command(mock(), mockMessage, listOf("test", "1", "2", "3", "-u", "<@200000000000000000>"))
    }

    "Command fails on invalid arguments" {
        val command = TestCommand { }

        command(mock(), mock(), listOf("invalid", "arguments")) shouldBe CommandFailed
    }
})