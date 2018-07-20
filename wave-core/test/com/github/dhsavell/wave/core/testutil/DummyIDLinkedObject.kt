package com.github.dhsavell.wave.core.testutil

import sx.blah.discord.handle.obj.IIDLinkedObject

class DummyIDLinkedObject(val id: Long) : IIDLinkedObject {
    override fun getLongID(): Long = id
    fun getOtherObject(otherID: Long): DummyIDLinkedObject = DummyIDLinkedObject(otherID)

    override fun equals(other: Any?): Boolean {
        return other is DummyIDLinkedObject && other.id == id
    }

    override fun hashCode(): Int {
        return id.hashCode().shl(7)
    }
}