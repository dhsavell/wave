package com.github.dhsavell.wave.core.command

import com.github.dhsavell.wave.core.command.factory.SimpleCommandFactory
import sx.blah.discord.handle.obj.IMessage

class NumberRepeaterCommand(context: IMessage) : AbstractBaseCommand(context) {
    val number by args.storing("number") { toInt() }

    override fun execute(): CommandResult {
        return CommandSuccededWithValue("number is $number")
    }

    companion object Factory : SimpleCommandFactory("number", listOf("num", "n"), ::NumberRepeaterCommand)
}