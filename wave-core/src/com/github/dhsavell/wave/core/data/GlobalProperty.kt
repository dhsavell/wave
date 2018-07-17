package com.github.dhsavell.wave.core.data

/**
 * Represents a mutable value that persists across everything (servers, users, channels, etc.) handled by the bot.
 * @param T Type being stored.
 */
interface GlobalProperty<T> {
    val name: String

    fun get(): T
    fun set(value: T)
}