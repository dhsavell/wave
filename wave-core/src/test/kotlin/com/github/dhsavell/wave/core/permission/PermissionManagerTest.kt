package com.github.dhsavell.wave.core.permission

import com.github.dhsavell.wave.core.command.Command
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import org.mapdb.DBMaker
import sx.blah.discord.handle.obj.IGuild

class PermissionManagerTest : StringSpec({
    val db = DBMaker.memoryDB().make()

    val mockCommand = mock<Command> {
        on { name } doReturn "command"
        on { defaultPermission } doReturn AnybodyCanUse
    }

    val mockGuild = mock<IGuild> {
        on { longID } doReturn 123456789098765432
    }

    "A PermissionManager with no set override returns a Command's default permission" {
        val manager = PermissionManager(db)
        manager.getEffectivePermission(mockCommand, mock()) shouldBe AnybodyCanUse
    }

    "Permission overrides can be set for a Command" {
        val manager = PermissionManager(db)
        manager.setOverride(mockCommand, NobodyCanUse, mockGuild)
        manager.getEffectivePermission(mockCommand, mock()) shouldBe NobodyCanUse
    }

    "Permission overrides can be removed" {
        val manager = PermissionManager(db)
        manager.setOverride(mockCommand, NobodyCanUse, mockGuild)
        manager.clearOverride(mockCommand, mockGuild)
        manager.getEffectivePermission(mockCommand, mockGuild) shouldBe AnybodyCanUse
    }
})