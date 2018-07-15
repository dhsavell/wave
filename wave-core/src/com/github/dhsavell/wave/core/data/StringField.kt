package com.github.dhsavell.wave.core.data

import com.sxtanna.database.Kedis
import sx.blah.discord.handle.obj.IGuild

class StringField(override val name: String, override val defaultValue: String) : DataField<String> {
    override fun getValue(guild: IGuild, db: Kedis): String? {
        var value: String? = null

        db {
            val keyName = guild.getDatabaseKey(name)
            if (!contains(keyName)) {
                set(keyName, defaultValue)
            }

            value = get(keyName)
        }

        return value
    }

    override fun setValue(value: String, guild: IGuild, db: Kedis) {
        db {
            set(guild.getDatabaseKey(name), value)
        }
    }
}