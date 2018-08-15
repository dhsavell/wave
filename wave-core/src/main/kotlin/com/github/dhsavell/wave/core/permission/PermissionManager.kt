package com.github.dhsavell.wave.core.permission

import com.github.dhsavell.wave.core.command.Command
import org.mapdb.DB
import org.mapdb.Serializer
import sx.blah.discord.handle.obj.IGuild
import sx.blah.discord.handle.obj.IUser
import java.util.concurrent.ConcurrentMap

/**
 * Manages Command permission overrides.
 */
class PermissionManager(private val db: DB) {
    /**
     * Gets the effective permission of a command within a guild. If an override has been set, that is returned.
     * Otherwise, the default permission for the command is returned.
     */
    fun getEffectivePermission(command: Command, guild: IGuild): Permission {
        val permissionMap = guild.getLivePermissionMap(db)
        return if (permissionMap.containsKey(command.name)) {
            Permission.fromLong(permissionMap[command.name] ?: 0L, guild)
        } else {
            command.defaultPermission
        }
    }

    /**
     * Sets a permission override for a command within a guild.
     */
    fun setOverride(command: Command, permission: Permission, guild: IGuild) {
        if (command.defaultPermission != permission) {
            val permissionMap = guild.getLivePermissionMap(db)
            permissionMap[command.name] = permission.toLong()
        }
    }

    /**
     * Clears the permission override for a command within a guild..
     */
    fun clearOverride(command: Command, guild: IGuild) {
        val permissionMap = guild.getLivePermissionMap(db)
        if (permissionMap.containsKey(command.name)) {
            permissionMap.remove(command.name)
        }
    }

    /**
     * Returns whether or not a user can invoke a command.
     */
    fun userCanInvoke(command: Command, user: IUser, guild: IGuild): Boolean =
            getEffectivePermission(command, guild).appliesToUser(user, guild)
}

/**
 * Gets a "live" (modifications will be saved) permission map from a guild.
 */
fun IGuild.getLivePermissionMap(db: DB): ConcurrentMap<String, Long> {
    return db.hashMap("permission-overrides-$stringID", Serializer.STRING, Serializer.LONG).createOrOpen()
}