package com.github.dhsavell.wave.core.command

import com.github.dhsavell.wave.core.bot.Bot
import com.xenomachina.argparser.ArgParser
import org.mapdb.DB
import sx.blah.discord.handle.obj.IMessage

abstract class ArgParserCommand<T>(override val name: String, override val category: Category) : Command {
    abstract fun createArgsObject(parser: ArgParser, context: IMessage): T
    abstract fun invoke(bot: Bot, db: DB, message: IMessage, args: T): CommandResult

    override fun invoke(bot: Bot, db: DB, message: IMessage, args: List<String>): CommandResult {
        return try {
            val parsedArgs = ArgParser(args.toTypedArray()).parseInto { createArgsObject(it, message) }
            invoke(bot, db, message, parsedArgs)
        } catch (e: Exception) {
            CommandFailed
        }
    }
}