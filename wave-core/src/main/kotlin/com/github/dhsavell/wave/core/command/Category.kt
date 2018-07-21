package com.github.dhsavell.wave.core.command

/**
 * A class to represent a category of commands. Used primarily for grouping commands and providing information, but
 * can also be used to disable groups of commands. Might have additional purpose in the future.
 */
interface Category {
    val name: String
    val description: String
}