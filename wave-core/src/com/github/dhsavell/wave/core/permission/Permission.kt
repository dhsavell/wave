package com.github.dhsavell.wave.core.permission

import sx.blah.discord.handle.obj.IGuild
import sx.blah.discord.handle.obj.IRole
import sx.blah.discord.handle.obj.IUser
import sx.blah.discord.handle.obj.Permissions

sealed class Permission(val name: String) {
    abstract fun appliesToUser(user: IUser, guild: IGuild): Boolean
}

object AnybodyCanUse : Permission("anybody") {
    override fun appliesToUser(user: IUser, guild: IGuild): Boolean = true
}

object NobodyCanUse : Permission("nobody") {
    override fun appliesToUser(user: IUser, guild: IGuild): Boolean = false
}

object ServerAdminsCanUse : Permission("admin") {
    override fun appliesToUser(user: IUser, guild: IGuild): Boolean {
        return user.getPermissionsForGuild(guild).contains(Permissions.ADMINISTRATOR)
    }
}

class MembersWithRoleCanUse(val role: IRole) : Permission("role") {
    override fun appliesToUser(user: IUser, guild: IGuild): Boolean {
        return user.hasRole(role)
    }
}