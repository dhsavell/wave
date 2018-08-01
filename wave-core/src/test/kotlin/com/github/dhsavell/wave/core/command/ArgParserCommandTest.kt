package com.github.dhsavell.wave.core.command

import com.github.dhsavell.wave.core.bot.Bot
import com.github.dhsavell.wave.core.testutil.DummyBot
import com.github.dhsavell.wave.core.util.toUserFromIdentifier
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.xenomachina.argparser.ArgParser
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import org.mapdb.DB
import sx.blah.discord.handle.obj.IGuild
import sx.blah.discord.handle.obj.IMessage
import sx.blah.discord.handle.obj.IUser

object TestCategory : Category {
    override val name: String = ""
    override val description: String = ""
}

class TestCommand(private val onInvoked: (Args) -> Unit) : ArgParserCommand<TestCommand.Args>("test", TestCategory) {
    override fun createArgsObject(parser: ArgParser, bot: Bot, context: IMessage): Args = Args(parser, context.guild)

    override fun invoke(bot: Bot, db: DB, message: IMessage, args: Args): CommandResult {
        onInvoked(args)
        return CommandSucceeded
    }

    class Args(parser: ArgParser, guild: IGuild) {
        val message by parser.storing("an arbitrary string message")
        val ints by parser.positionalList("a list of some ints")
        val flag by parser.flagging("a boolean flag")
        val user by parser.storing("a discord user") { toUserFromIdentifier(guild) }
    }
}

class ArgParserCommandTest : StringSpec({
    val mockBot = DummyBot()

    "Parameters can be passed to an ArgParserCommand" {
        val command = TestCommand { args ->
            args.message shouldBe "test"
            args.ints shouldBe arrayOf(1, 2, 3)
            args.flag shouldBe false
        }

        command(mockBot, mock(), mock(), listOf("test", "1", "2", "3"))
    }

    "Command fails when invalid parameters are passed" {
        val command = TestCommand { }
        command(mockBot, mockBot.db, mock(), listOf("test", "sdgfdfs", "--sdfarg", "Asdfrg")) shouldBe CommandFailed
    }

    "Command fails when an argument can't be parsed" {
        val command = TestCommand { }
        command(mockBot, mockBot.db, mock(), listOf("test", "1", "-u", "not-a-user")) shouldBe CommandFailed
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

        command(mockBot, mock(), mockMessage, listOf("test", "1", "2", "3", "-u", "bar"))
        command(mockBot, mock(), mockMessage, listOf("test", "1", "2", "3", "-u", "200000000000000000"))
        command(mockBot, mock(), mockMessage, listOf("test", "1", "2", "3", "-u", "<@200000000000000000>"))
    }
})