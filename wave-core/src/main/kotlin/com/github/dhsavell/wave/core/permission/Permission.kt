package com.github.dhsavell.wave.core.permission

import sx.blah.discord.handle.obj.IGuild
import sx.blah.discord.handle.obj.IRole
import sx.blah.discord.handle.obj.IUser
import sx.blah.discord.handle.obj.Permissions

sealed class Permission(val name: String) {
    abstract fun appliesToUser(user: IUser, guild: IGuild): Boolean
    abstract fun toLong(): Long

    companion object {
        fun fromLong(long: Long, guild: IGuild): Permission {
            // Values 0, 1, and 2 can be used for our own purposes since they are invalid role IDs.
            return when (long) {
                0L -> NobodyCanUse
                1L -> AnybodyCanUse
                2L -> ServerAdminsCanUse
                else -> {
                    val storedRole = guild.getRoleByID(long)
                    return if (storedRole != null) {
                        MembersWithRoleCanUse(storedRole)
                    } else {
                        NobodyCanUse
                    }
                }
            }
        }
    }
}

object AnybodyCanUse : Permission("anybody") {
    override fun toLong(): Long = 1

    override fun appliesToUser(user: IUser, guild: IGuild): Boolean = true
}

object NobodyCanUse : Permission("nobody") {
    override fun toLong(): Long = 0

    override fun appliesToUser(user: IUser, guild: IGuild): Boolean = user == guild.owner
}

object ServerAdminsCanUse : Permission("admin") {
    override fun toLong(): Long = 2

    override fun appliesToUser(user: IUser, guild: IGuild): Boolean {
        return user == guild.owner || user.getPermissionsForGuild(guild).contains(Permissions.ADMINISTRATOR)
    }
}

class MembersWithRoleCanUse(val role: IRole) : Permission("role") {
    override fun toLong(): Long = role.longID

    override fun appliesToUser(user: IUser, guild: IGuild): Boolean {
        return user == guild.owner || user.hasRole(role)
    }
}