package com.github.dhsavell.wave.core.command

import com.github.dhsavell.wave.core.permission.AllUsers
import com.github.dhsavell.wave.core.permission.Permission
import com.github.dhsavell.wave.core.permission.PermissionBoundAction
import org.dizitart.no2.Nitrite
import sx.blah.discord.handle.obj.IMessage

/**
 * A simple CommandFactory implementation for matching commands by name.
 */
open class SimpleCommandFactory(
    private val primaryCommandName: String,
    private val commandAliases: List<String> = emptyList(),
    private val commandProvider: (String, IMessage, Nitrite) -> Command,
    override val defaultPermission: Permission = AllUsers
) : CommandFactory, PermissionBoundAction {
    override val permissionDescriptor: String = primaryCommandName

    override fun canProvideFor(commandName: String, context: IMessage, db: Nitrite): Boolean {
        return primaryCommandName.equals(commandName, ignoreCase = true) ||
            (commandAliases.isNotEmpty() &&
                commandAliases.map { alias -> alias.toLowerCase() }.contains(commandName.toLowerCase()))
    }

    override fun getAction(commandCall: String, context: IMessage, db: Nitrite): Command =
        commandProvider(commandCall, context, db)
}