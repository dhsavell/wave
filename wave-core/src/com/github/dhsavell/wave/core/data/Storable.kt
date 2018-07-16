package com.github.dhsavell.wave.core.data

import com.sxtanna.database.Kedis

interface Storable<out T> {
    val name: String
    fun loadFromDatabase(db: Kedis): T
    fun saveToDatabase(db: Kedis)
}

interface StorableAsString<out T> : Storable<T> {
    override fun saveToDatabase(db: Kedis) {
        db {
            set(name, toString())
        }
    }
}

interface StorableAsStringMap<out T> : Storable<T> {
    fun toStringMap(): Map<String, String>

    override fun saveToDatabase(db: Kedis) {
        db {
            toStringMap().forEach { key, value ->
                setHash(name, key, value)
            }
        }
    }
}