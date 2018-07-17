package com.github.dhsavell.wave.app.provider

import com.github.dhsavell.wave.core.command.Command
import net.java.sezpoz.Index
import net.java.sezpoz.Indexable

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.FIELD)
@Indexable(type = Command::class)
annotation class BotListener

fun provideListeners(): List<Command> {
    return Index.load(BotCommand::class.java, Command::class.java).map { command -> command.instance() }
}