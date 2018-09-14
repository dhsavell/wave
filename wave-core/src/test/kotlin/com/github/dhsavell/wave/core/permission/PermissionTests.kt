package com.github.dhsavell.wave.core.permission

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import sx.blah.discord.handle.obj.IGuild
import sx.blah.discord.handle.obj.IUser
import sx.blah.discord.handle.obj.Permissions
import java.util.EnumSet

class PermissionTests : StringSpec({

    val unprivilegedUser = mock<IUser> {
        on { hasRole(any()) } doReturn false
        on { getPermissionsForGuild(any()) } doReturn EnumSet.noneOf(Permissions::class.java)
    }

    val serverAdminUser = mock<IUser> {
        on { hasRole(any()) } doReturn false
        on { getPermissionsForGuild(any()) } doReturn EnumSet.of(Permissions.ADMINISTRATOR)
    }

    val roleUser = mock<IUser> {
        on { hasRole(any()) } doReturn true
        on { getPermissionsForGuild(any()) } doReturn EnumSet.noneOf(Permissions::class.java)
    }

    val ownerUser = mock<IUser> {
        on { hasRole(any()) } doReturn false
        on { getPermissionsForGuild(any()) } doReturn EnumSet.allOf(Permissions::class.java)
    }

    val mockGuild = mock<IGuild> {
        on { owner } doReturn ownerUser
    }

    """The "anybody" permission applies to any user""" {
        listOf(unprivilegedUser, serverAdminUser, roleUser).all {
            AnybodyCanUse.appliesToUser(it, mockGuild)
        } shouldBe true
    }

    """The "nobody" permission applies to no users""" {
        listOf(unprivilegedUser, serverAdminUser, roleUser).any {
            NobodyCanUse.appliesToUser(it, mockGuild)
        } shouldBe false
    }

    """The "server administrators" permission applies to server administrators""" {
        listOf(unprivilegedUser, roleUser).any {
            ServerAdminsCanUse.appliesToUser(it, mockGuild)
        } shouldBe false

        ServerAdminsCanUse.appliesToUser(serverAdminUser, mockGuild) shouldBe true
    }

    """The "server role" permission applies to users with the specified role""" {
        val permission = MembersWithRoleCanUse(mock())
        listOf(unprivilegedUser, serverAdminUser).any {
            permission.appliesToUser(it, mockGuild)
        } shouldBe false

        permission.appliesToUser(roleUser, mockGuild) shouldBe true
    }
})