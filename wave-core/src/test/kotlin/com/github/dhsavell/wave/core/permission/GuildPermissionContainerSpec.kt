package com.github.dhsavell.wave.core.permission

import com.github.dhsavell.wave.core.testutil.guild
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec
import org.dizitart.kno2.nitrite

class GuildPermissionContainerSpec : WordSpec({
    val db = nitrite { }

    "A permission container" should {
        "be able to save and load permission overrides" {
            val container = GuildPermissionContainer(guild(id = 0), db)

            container.updatePermissionOverrideFor("something", NoUsers)
            container.getPermissionFor("something", AllUsers) shouldBe NoUsers
        }

        "return a provided fallback value if a permission override doesn't exist" {
            val container = GuildPermissionContainer(guild(id = 1), db)

            container.getPermissionFor("something", AllUsers) shouldBe AllUsers
        }

        "be able to remove existing permission overrides" {
            val container = GuildPermissionContainer(guild(id = 2), db)

            container.updatePermissionOverrideFor("something", NoUsers)
            container.getPermissionFor("something", AllUsers) shouldBe NoUsers
            container.removePermissionOverrideFor("something")
            container.getPermissionFor("something", AllUsers) shouldBe AllUsers
        }
    }
})