package com.github.dhsavell.wave.core.command

/**
 * An interface representing a category of commands. May have additional functionality in the future.
 */
interface Category {
    val name: String
    val description: String
}