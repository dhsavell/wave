package com.github.dhsavell.wave.core.permission

import org.dizitart.kno2.documentOf
import org.dizitart.kno2.filters.eq
import org.dizitart.kno2.getCollection
import org.dizitart.no2.Nitrite
import sx.blah.discord.handle.obj.IGuild
import sx.blah.discord.handle.obj.IUser

/**
 * A wrapper for interfacing with a Nitrite database to save and load permission information. For example, this could
 * mean overriding the default required permission for a command within a server.
 */
class GuildPermissionContainer(val guild: IGuild, val db: Nitrite) {
    private val collectionName = "p_${guild.stringID}"

    /**
     * Updates the permission override for a given descriptor.
     */
    fun updatePermissionOverrideFor(descriptor: String, permission: Permission) {
        db.getCollection(collectionName) {
            insert(documentOf("descriptor" to descriptor, "permission" to permission))
        }
    }

    /**
     * Removes the permission override for a given descriptor.
     */
    fun removePermissionOverrideFor(descriptor: String) {
        db.getCollection(collectionName) {
            remove("descriptor" eq descriptor)
        }
    }

    /**
     * Gets the permission for a given descriptor. If an overridden permission for the given descriptor doesn't exist,
     * then the supplied fallback permission will be returned.
     */
    fun getPermissionFor(descriptor: String, fallback: Permission = NoUsers): Permission {
        val permissions = db.getCollection(collectionName)
        return permissions
            .find("descriptor" eq descriptor)
            .firstOrNull()?.get("permission", Permission::class.java) ?: fallback
    }

    /**
     * Determines whether or not a given user has the permission associated with a given descriptor. Similarly to
     * getPermissionFor, the supplied fallback permission will be used in the event that an override doesn't exist.
     */
    fun userHasPermission(
        user: IUser,
        descriptor: String,
        defaultPermission: Permission = NoUsers
    ): Boolean =
        getPermissionFor(descriptor, defaultPermission).userIsPrivileged(user)
}