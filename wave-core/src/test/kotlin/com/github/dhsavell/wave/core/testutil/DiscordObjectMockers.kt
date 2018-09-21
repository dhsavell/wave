package com.github.dhsavell.wave.core.testutil

import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import sx.blah.discord.handle.obj.IGuild
import sx.blah.discord.handle.obj.IMessage
import sx.blah.discord.handle.obj.IRole
import sx.blah.discord.handle.obj.IUser

fun message(content: String = ""): IMessage {
    val message = mockk<IMessage>()
    every { message.content } returns content

    return message
}

fun role(name: String, id: Long = 12345): IRole {
    val role = mockk<IRole>()

    every { role.name } returns name
    every { role.longID } returns id

    return role
}

fun user(name: String = "", discriminator: String = "0000", id: Long = 12345, roles: List<IRole> = emptyList()): IUser {
    val user = mockk<IUser>()

    every { user.name } returns name
    every { user.discriminator } returns discriminator
    every { user.longID } returns id
    every { user.getRolesForGuild(any()) } returns roles

    val roleCheckSlot = slot<IRole>()
    every { user.hasRole(capture(roleCheckSlot)) } answers { roles.contains(roleCheckSlot.captured) }

    return user
}

fun guild(name: String = "", id: Long = 12345): IGuild {
    val guild = mockk<IGuild>()

    every { guild.name } returns name
    every { guild.longID } returns id
    every { guild.stringID } returns id.toString()

    return guild
}