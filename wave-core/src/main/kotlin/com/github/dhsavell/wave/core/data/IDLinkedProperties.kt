package com.github.dhsavell.wave.core.data

import org.mapdb.DB
import org.mapdb.Serializer
import sx.blah.discord.handle.obj.IIDLinkedObject

/**
 * A DomainProperty for storing an IDLinkedObject (i.e. Discord4J objects such as IUser, IChannel...) within an IGuild.
 *
 * @see IDLinkedListProperty
 * @param K Domain this property will be accessed from.
 * @param V Type being stored in this property.
 * @param name Name of this property, used in storage.
 * @param transformer Function to restore an object from its Long ID.
 */
open class IDLinkedProperty<K : IIDLinkedObject, V : IIDLinkedObject>(
    override val name: String,
    private val transformer: (K) -> (Long) -> V
) : DomainProperty<K, V> {
    override fun getPropertyValue(db: DB, domain: K): V? {
        val map = db.hashMap(name, Serializer.LONG, Serializer.LONG).createOrOpen()
        val id = map[domain.longID]

        return id?.let(transformer(domain))
    }

    override fun setPropertyValue(db: DB, domain: K, value: V) {
        val map = db.hashMap(name, Serializer.LONG, Serializer.LONG).createOrOpen()
        map[domain.longID] = value.longID
    }
}

/**
 * A DomainProperty for storing an List of IDLinkedObjects within an IGuild. This has nothing to do with linked lists.
 *
 * @see IDLinkedProperty
 * @param K Domain this property will be accessed from.
 * @param V Type being stored in this property.
 * @param name Name of this property, used in storage.
 * @param transformer Function to restore an object from its Long ID.
 */
open class IDLinkedListProperty<K : IIDLinkedObject, V : IIDLinkedObject>(
    override val name: String,
    private val transformer: (K) -> (Long) -> V
) : DomainProperty<K, List<V>> {
    override fun getPropertyValue(db: DB, domain: K): List<V>? {
        val map = db.hashMap(name, Serializer.LONG, Serializer.LONG_ARRAY).createOrOpen()
        val ids = map[domain.longID]

        return ids?.map { transformer(domain)(it) }
    }

    override fun setPropertyValue(db: DB, domain: K, value: List<V>) {
        val map = db.hashMap(name, Serializer.LONG, Serializer.LONG_ARRAY).createOrOpen()
        map[domain.longID] = value.map(IIDLinkedObject::getLongID).toLongArray()
    }

    fun appendValues(db: DB, domain: K, vararg value: V) {
        val currentValue = getPropertyValue(db, domain)
        if (currentValue == null) {
            setPropertyValue(db, domain, value.toList())
        } else {
            setPropertyValue(db, domain, currentValue + value)
        }
    }
}