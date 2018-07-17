package com.github.dhsavell.wave.core.data

import sx.blah.discord.handle.obj.IChannel
import sx.blah.discord.handle.obj.IDiscordObject
import sx.blah.discord.handle.obj.IGuild
import sx.blah.discord.handle.obj.IUser

/**
 * Represents a mutable value that exists within a single domain of any type. This could be an IGuild or an IUser, for
 * example.
 * @param D Type of the value's domain.
 * @param T Type being stored.
 */
interface Property<in D, T> {
    val localName: String

    fun getDefaultValue(domain: D): T
    fun get(domain: D): T
    fun set(domain: D, value: T)

    /**
     * Convenience infix method for accessing this Property's value in a readable way. Equivalent to Property#get.
     */
    infix fun within(domain: D): T = get(domain)
}

public fun IDiscordObject<*>.getPropertyPrefix(): String {
    return when (this) {
        is IUser -> "USER-$stringID"
        is IChannel -> "CHANNEL-$stringID"
        is IGuild -> "GUILD-$stringID"
        else -> "OTHER-" + getStringID()
    }
}

public fun IDiscordObject<*>.getPropertyKey(propertyName: String): String {
    return getPropertyPrefix() + "." + propertyName
}