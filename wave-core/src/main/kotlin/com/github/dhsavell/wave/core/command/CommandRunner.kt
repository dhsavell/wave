package com.github.dhsavell.wave.core.command

import com.github.dhsavell.wave.core.permission.GuildPermissionContainer
import com.github.dhsavell.wave.core.permission.InsufficientPermissionException
import com.github.dhsavell.wave.core.permission.PermissionBoundAction
import com.xenomachina.argparser.SystemExitException
import org.dizitart.no2.Nitrite
import sx.blah.discord.handle.obj.IMessage

/**
 * A container of CommandFactories responsible for calling defined commands by name.
 * @param commandFactories List of available CommandFactories
 */
class CommandRunner(val commandFactories: List<CommandFactory>) {

    /**
     * Attempts to run a command given a call (with no additional prefix) and message context. If a CommandFactory is
     * an instance of a PermissionBoundAction, its permissions will be checked.
     *
     * @param callWithoutPrefix Command call to run
     * @param context Message providing context about the executing environment
     */
    fun runCommand(
        callWithoutPrefix: String,
        context: IMessage,
        db: Nitrite,
        skipPermissionCheck: Boolean = false
    ): CommandResult {
        val commandName = callWithoutPrefix.split(" ")[0]
        val correspondingFactory =
            commandFactories.find { factory -> factory.canProvideFor(commandName, context, db) }
                ?: return CommandNotFound

        if (correspondingFactory is PermissionBoundAction && context.guild.owner != context.author && !skipPermissionCheck) {
            val effectivePermission = GuildPermissionContainer(context.guild, db).getPermissionFor(
                correspondingFactory.permissionDescriptor,
                correspondingFactory.defaultPermission
            )

            if (!effectivePermission.userIsPrivileged(context.author)) {
                return CommandFailedWithException(InsufficientPermissionException(effectivePermission))
            }
        }

        return try {
            correspondingFactory.getAction(callWithoutPrefix, context, db).execute()
        } catch (e: SystemExitException) {
            CommandFailedWithException(InvalidCommandSyntaxException(e.message ?: "invalid command syntax", e))
        }
    }
}