package com.github.dhsavell.wave.core.data

import org.mapdb.DB
import org.mapdb.Serializer
import sx.blah.discord.handle.obj.IChannel
import sx.blah.discord.handle.obj.IGuild
import sx.blah.discord.handle.obj.IIDLinkedObject
import sx.blah.discord.handle.obj.IUser

/**
 * A DomainProperty for storing an IDLinkedObject (i.e. Discord4J objects such as IUser, IChannel...) within an IGuild.
 * Ideally, one of the predefined implementations should be used.
 *
 * @see IDLinkedArrayProperty
 * @see GuildUserProperty
 * @see GuildChannelProperty
 * @param K Domain this property will be accessed from.
 * @param V Type being stored in this property.
 * @param name Name of this property, used in storage.
 * @param transformer Function to restore an object from its Long ID.
 */
open class IDLinkedProperty<K : IIDLinkedObject, V : IIDLinkedObject>(override val name: String,
                                                                      private val transformer: (K) -> (Long) -> V) : DomainProperty<K, V> {
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
 * A DomainProperty for storing an Array of IDLinkedObjects within an IGuild.
 *
 * @see IDLinkedProperty
 * @see GuildUserArrayProperty
 * @see GuildChannelArrayProperty
 * @param K Domain this property will be accessed from.
 * @param V Type being stored in this property.
 * @param name Name of this property, used in storage.
 * @param transformer Function to restore an object from its Long ID.
 */
open class IDLinkedArrayProperty<K : IIDLinkedObject, V : IIDLinkedObject>(override val name: String,
                                                                           private val transformer: (K) -> (Long) -> V) : DomainProperty<K, List<V>> {
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


/**
 * An IDLinkedProperty for storing an IUser within an IGuild.
 */
data class GuildUserProperty(override val name: String) : IDLinkedProperty<IGuild, IUser>(name,
        { guild -> guild::getUserByID })

/**
 * An IDLinkedProperty for storing an array of IUsers.
 */
data class GuildUserArrayProperty(override val name: String) : IDLinkedArrayProperty<IGuild, IUser>(name,
        { guild -> guild::getUserByID })

/**
 * An IDLinkedProperty for storing an IChannel within an IGuild.
 */
data class GuildChannelProperty(override val name: String) : IDLinkedProperty<IGuild, IChannel>(name,
        { guild -> guild::getChannelByID })

/**
 * An IDLinkedProperty for storing an array of IChannels.
 */
data class GuildChannelArrayProperty(override val name: String) : IDLinkedArrayProperty<IGuild, IChannel>(name,
        { guild -> guild::getChannelByID })