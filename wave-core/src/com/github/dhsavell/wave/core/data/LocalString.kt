package com.github.dhsavell.wave.core.data

import com.sxtanna.database.Kedis
import sx.blah.discord.handle.obj.IDiscordObject

class LocalString<D : IDiscordObject<D>>(private val db: Kedis, override val localName: String,
                                         private val defaultValue: String) : Property<D, String> {
    override fun getDefaultValue(domain: D): String {
        return defaultValue
    }

    override fun get(domain: D): String {
        var storedValue = defaultValue

        db {
            val keyName = domain.getPropertyKey(localName)
            if (contains(keyName)) {
                storedValue = this@db[keyName] ?: defaultValue
            }
        }

        return storedValue
    }

    override fun set(domain: D, value: String) {
        db {
            this@db[domain.getPropertyKey(localName)] = value
        }
    }
}