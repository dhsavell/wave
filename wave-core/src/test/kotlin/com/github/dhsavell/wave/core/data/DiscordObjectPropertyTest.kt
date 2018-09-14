package com.github.dhsavell.wave.core.data

import com.github.dhsavell.wave.core.testutil.DummyIDLinkedObject
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import org.mapdb.DBMaker

class DiscordObjectPropertyTest : StringSpec({
    val db = DBMaker.memoryDB().make()

    val domain1 = DummyIDLinkedObject(1)

    val value1 = DummyIDLinkedObject(11)

    "An DiscordObjectProperty returns null when its value isn't found" {
        val property = DiscordObjectProperty<DummyIDLinkedObject, DummyIDLinkedObject>(name()) { it::getOtherObject }
        property.getPropertyValue(db, domain1) shouldBe null
    }

    "A value can be written to and loaded from an DiscordObjectProperty" {
        val property = DiscordObjectProperty<DummyIDLinkedObject, DummyIDLinkedObject>(name()) { it::getOtherObject }
        property.setPropertyValue(db, domain1, value1)
        property.getPropertyValue(db, domain1) shouldBe value1
    }
})
