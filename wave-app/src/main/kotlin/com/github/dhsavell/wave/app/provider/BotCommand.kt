package com.github.dhsavell.wave.app.provider

import com.github.dhsavell.wave.core.command.Command

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.FIELD)
@Indexable(type = Command::class)
annotation class BotCommand

fun provideCommands(): List<Command> {
    return Index.load(BotCommand::class.java, Command::class.java).map { command -> command.instance() }
}