package com.github.dhsavell.wave.core.util

import sx.blah.discord.handle.obj.IChannel
import sx.blah.discord.handle.obj.IGuild
import sx.blah.discord.handle.obj.IMessage
import sx.blah.discord.handle.obj.IRole
import sx.blah.discord.handle.obj.IUser

const val DISCORD_ID_LENGTH = 18

fun String.toUserIDFromMention(): String {
    return this.replace("[<@>]".toRegex(), "")
}

fun String.toChannelIDFromMention(): String {
    return this.replace("[<#>]".toRegex(), "")
}

fun String.toRoleIDFromMention(): String {
    return this.replace("[<@&>]".toRegex(), "")
}

private fun <T> getValueFromIdentifier(
    identifier: String,
    nameMatcher: (String) -> T?,
    stringIDGetter: (String) -> String,
    idMatcher: (Long) -> T?
): T? {
    val matchByName = nameMatcher(identifier)
    if (matchByName != null) {
        return matchByName
    }

    val stringID = stringIDGetter(identifier)
    if (stringID.length != DISCORD_ID_LENGTH || stringID.toLongOrNull() == null) {
        return null
    }

    return idMatcher(stringID.toLong())
}

/**
 * Gets a user from a given identifier. This could be a mention, ID, or some portion of their display name.
 */
fun String.toUserFromIdentifier(guild: IGuild): IUser? = getValueFromIdentifier(
    this, { name -> guild.users.find { it.name.startsWith(name, true) } },
    String::toUserIDFromMention, guild::getUserByID
)

/**
 * Gets a channel from a given identifier. This could be a mention, ID, or some portion of its display name.
 */
fun String.toChannelFromIdentifier(guild: IGuild): IChannel? = getValueFromIdentifier(
    this, { name -> guild.channels.find { it.name.startsWith(name, true) } },
    String::toChannelIDFromMention, guild::getChannelByID
)

/**
 * Gets a role from a given identifier. This could be a mention, ID, or some portion of its display name.
 */
fun String.toRoleFromIdentifier(guild: IGuild): IRole? = getValueFromIdentifier(
    this, { name -> guild.roles.find { it.name.startsWith(name, true) } },
    String::toRoleIDFromMention, guild::getRoleByID
)

fun String.toMessageFromID(channel: IChannel): IMessage? = toLongOrNull()?.let { channel.getMessageByID(it) }