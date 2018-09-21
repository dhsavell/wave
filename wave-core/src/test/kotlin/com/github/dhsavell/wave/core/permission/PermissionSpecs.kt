package com.github.dhsavell.wave.core.permission

import com.github.dhsavell.wave.core.testutil.role
import com.github.dhsavell.wave.core.testutil.user
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

class PermissionSpecs : WordSpec({
    "AllUsers" should {
        "allow permission for any user" {
            AllUsers.userIsPrivileged(user()) shouldBe true
        }
    }

    "NoUsers" should {
        "not allow permission for any user" {
            NoUsers.userIsPrivileged(user()) shouldBe false
        }
    }

    "UsersWithRoleOnly" should {
        "only allow permission for users with a specified role" {
            val adminRole = role("Administrators", id = 12345)
            val memberRole = role("Members", id = 67890)

            val testPermission = UsersWithRoleOnly(adminRole)

            testPermission.userIsPrivileged(user()) shouldBe false
            testPermission.userIsPrivileged(user(roles = listOf(memberRole))) shouldBe false
            testPermission.userIsPrivileged(user(roles = listOf(adminRole))) shouldBe true
            testPermission.userIsPrivileged(user(roles = listOf(memberRole, adminRole))) shouldBe true
        }
    }
})