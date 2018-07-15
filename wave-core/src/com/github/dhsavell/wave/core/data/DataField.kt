package com.github.dhsavell.wave.core.data

import com.sxtanna.database.Kedis
import sx.blah.discord.handle.obj.IChannel
import sx.blah.discord.handle.obj.IGuild
import sx.blah.discord.handle.obj.IUser

const val GUILD_PREFIX = "guild"
const val CHANNEL_PREFIX = "channel"
const val USER_PREFIX = "user"

/**
 * An interface for saving and loading data to a Redis database.
 */
interface DataField<T> {
    /**
     * Default responseValue of this DataField.
     */
    val defaultValue: T

    /**
     * Name of this DataField.
     */
    val name: String

    /**
     * Gets the responseValue of this DataField from a given guild and database.
     */
    fun getValue(guild: IGuild, db: Kedis): T?

    /**
     * Sets the responseValue of this DataField to a given guild and database.
     */
    fun setValue(value: T, guild: IGuild, db: Kedis)
}

fun IGuild.getDatabaseKey(keyName: String): String {
    return GUILD_PREFIX + "." + this.stringID + "." + keyName
}

fun IChannel.getDatabaseKey(keyName: String): String {
    return CHANNEL_PREFIX + "." + this.stringID + "." + keyName
}

fun IUser.getDatabaseKey(keyName: String): String {
    return USER_PREFIX + "." + this.stringID + "." + keyName
}