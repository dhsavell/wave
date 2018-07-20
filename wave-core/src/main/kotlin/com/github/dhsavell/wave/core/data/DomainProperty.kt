package com.github.dhsavell.wave.core.data

import org.mapdb.DB
import sx.blah.discord.handle.obj.IIDLinkedObject

/**
 * A wrapper for storing some value within a domain. A domain refers to some kind of Discord object-- in most cases,
 * a server (internally referred to as a Guild). This wrapper was designed with Command objects in mind, allowing for
 * simple access to data storage.
 *
 * @param K Domain type of this property.
 * @param V Value type of this property.
 */
interface DomainProperty<K : IIDLinkedObject, V> {
    val name: String

    /**
     * Gets the value of this property from a given database and domain.
     */
    fun getPropertyValue(db: DB, domain: K): V?

    /**
     * Sets this property to a given value within a given database and domain.
     */
    fun setPropertyValue(db: DB, domain: K, value: V)

    /**
     * Gets this property's value or a given default value if it is null.
     * @see getPropertyValue
     */
    fun getPropertyValue(db: DB, domain: K, default: V): V {
        return getPropertyValue(db, domain) ?: default
    }
}



