package com.github.dhsavell.wave.core.command.impl

import com.github.dhsavell.wave.core.command.AbstractBaseCommand
import com.github.dhsavell.wave.core.command.CommandResult
import com.github.dhsavell.wave.core.command.CommandSucceededWithValue
import com.github.dhsavell.wave.core.command.SimpleCommandFactory
import com.github.dhsavell.wave.core.permission.NoUsers
import org.dizitart.no2.Nitrite
import sx.blah.discord.handle.obj.IMessage

class AdministrativeCommand(commandCall: String, context: IMessage, db: Nitrite) :
    AbstractBaseCommand(commandCall, context, db) {

    override fun execute(): CommandResult = CommandSucceededWithValue("success")

    companion object Factory : SimpleCommandFactory("admin", listOf("a"), ::AdministrativeCommand, NoUsers)
}