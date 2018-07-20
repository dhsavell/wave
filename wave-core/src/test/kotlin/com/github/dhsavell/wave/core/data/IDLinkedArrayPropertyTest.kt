package com.github.dhsavell.wave.core.data

import com.github.dhsavell.wave.core.testutil.DummyIDLinkedObject
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import org.mapdb.DBMaker

class IDLinkedArrayPropertyTest : StringSpec({
    val db = DBMaker.memoryDB().make()

    val domain1 = DummyIDLinkedObject(1)

    val value1 = DummyIDLinkedObject(11)
    val value2 = DummyIDLinkedObject(12)
    val value3 = DummyIDLinkedObject(13)

    "An IDLinkedArrayProperty returns null when its value isn't found" {
        val property = IDLinkedArrayProperty<DummyIDLinkedObject, DummyIDLinkedObject>("null") { it::getOtherObject }
        property.getPropertyValue(db, domain1) shouldBe null
    }

    "A value can be written to and loaded from an IDLinkedArrayProperty" {
        val property = IDLinkedArrayProperty<DummyIDLinkedObject, DummyIDLinkedObject>("save-load") { it::getOtherObject }
        property.setPropertyValue(db, domain1, listOf(value1))
        property.getPropertyValue(db, domain1) shouldBe listOf(value1)
    }

    "An IDLinkedArrayProperty can be appended to" {
        val property = IDLinkedArrayProperty<DummyIDLinkedObject, DummyIDLinkedObject>("append") { it::getOtherObject }
        property.setPropertyValue(db, domain1, listOf(value1, value2))
        property.appendValues(db, domain1, value3, value1)
        property.getPropertyValue(db, domain1) shouldBe listOf(value1, value2, value3, value1)
    }

    "Appending to an empty IDLinkedArrayProperty initializes it" {
        val property = IDLinkedArrayProperty<DummyIDLinkedObject, DummyIDLinkedObject>("append-init") { it::getOtherObject }
        property.appendValues(db, domain1, value1)
        property.getPropertyValue(db, domain1) shouldBe listOf(value1)
    }
})