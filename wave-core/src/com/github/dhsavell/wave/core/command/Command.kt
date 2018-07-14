package com.github.dhsavell.wave.core.command

import net.java.sezpoz.Indexable

/**
 * Annotation for marking a UserCommand as automatically loadable.
 * @param name Name of the command, used both for display and invocation.
 * @param aliases List of the command's aliases, used for invocation.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
@Indexable(type = UserCommand::class)
annotation class Command(val name: String, val aliases: Array<String> = [])