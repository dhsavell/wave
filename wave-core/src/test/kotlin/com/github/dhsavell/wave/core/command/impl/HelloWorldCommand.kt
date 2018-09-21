package com.github.dhsavell.wave.core.command.impl

import com.github.dhsavell.wave.core.command.AbstractBaseCommand
import com.github.dhsavell.wave.core.command.CommandResult
import com.github.dhsavell.wave.core.command.CommandSucceededWithValue
import com.github.dhsavell.wave.core.command.SimpleCommandFactory
import com.xenomachina.argparser.default
import org.dizitart.no2.Nitrite
import sx.blah.discord.handle.obj.IMessage

class HelloWorldCommand(commandCall: String, context: IMessage, db: Nitrite) :
    AbstractBaseCommand(commandCall, context, db) {
    val name by args.storing("name").default("World")

    override fun execute(): CommandResult =
        CommandSucceededWithValue("Hello $name!")

    companion object Factory : SimpleCommandFactory("hello", listOf("h"), ::HelloWorldCommand)
}