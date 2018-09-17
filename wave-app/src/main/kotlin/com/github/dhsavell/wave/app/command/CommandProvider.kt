package com.github.dhsavell.wave.app.command

import com.github.dhsavell.wave.core.command.Command
import net.java.sezpoz.Indexable

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Indexable(type = Command::class)
annotation class CommandProvider
