package com.github.dhsavell.wave.core.command

import net.java.sezpoz.Indexable

/**
 * Annotation for marking a Command as automatically loadable.
 * @param name Name of the command, used both for display and invocation.
 * @param aliases List of the command's aliases, used for invocation.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
@Indexable(type = Command::class)
annotation class CommandModule(val name: String, val aliases: Array<String> = [])