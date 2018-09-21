package com.github.dhsavell.wave.core.permission

import sx.blah.discord.handle.obj.IRole
import sx.blah.discord.handle.obj.IUser
import java.io.Serializable

/**
 * Represents a permission, which is used to determine whether or not a user is permitted to do some action.
 */
sealed class Permission : Serializable {
    abstract fun userIsPrivileged(user: IUser): Boolean
}

/**
 * A permission that is granted to all users.
 */
object AllUsers : Permission() {
    override fun userIsPrivileged(user: IUser) = true
    override fun toString(): String = "Permission: All users"
}

/**
 * A permission that is granted to no users, resulting in an action only executable by the server owner (who is above
 * permission checks).
 */
object NoUsers : Permission() {
    override fun userIsPrivileged(user: IUser): Boolean = false
    override fun toString(): String = "Permission: Server owner only"
}

/**
 * A permission that is granted only to users with a certain role.
 */
class UsersWithRoleOnly(val role: IRole) : Permission() {
    override fun userIsPrivileged(user: IUser): Boolean = user.hasRole(role)
    override fun toString(): String = "Permission: Users with role $role"
}