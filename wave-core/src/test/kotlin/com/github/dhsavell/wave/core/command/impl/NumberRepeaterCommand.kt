package com.github.dhsavell.wave.core.command.impl

import com.github.dhsavell.wave.core.command.AbstractBaseCommand
import com.github.dhsavell.wave.core.command.CommandResult
import com.github.dhsavell.wave.core.command.CommandSucceededWithValue
import com.github.dhsavell.wave.core.command.SimpleCommandFactory
import org.dizitart.no2.Nitrite
import sx.blah.discord.handle.obj.IMessage

class NumberRepeaterCommand(commandCall: String, context: IMessage, db: Nitrite) :
    AbstractBaseCommand(commandCall, context, db) {
    private val number by args.storing("number") { toInt() }

    override fun execute(): CommandResult {
        return CommandSucceededWithValue("number is $number")
    }

    companion object Factory : SimpleCommandFactory("number", listOf("num", "n"), ::NumberRepeaterCommand)
}