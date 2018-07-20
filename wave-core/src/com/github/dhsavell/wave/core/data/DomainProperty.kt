package com.github.dhsavell.wave.core.data

import org.mapdb.DB
import sx.blah.discord.handle.obj.IIDLinkedObject

/**
 * A wrapper for storing some value within a domain. A domain refers to some kind of Discord object-- in most cases,
 * a server (internally referred to as a Guild). This wrapper was designed with Command objects in mind,
 */
interface DomainProperty<K : IIDLinkedObject, V> {
    val name: String

    fun getPropertyValue(db: DB, domain: K): V?
    fun setPropertyValue(db: DB, domain: K, value: V)

    fun getPropertyValue(db: DB, domain: K, default: V): V {
        return getPropertyValue(db, domain) ?: default
    }
}



