package com.github.dhsavell.wave.app

import com.github.dhsavell.wave.core.command.CommandFactory
import net.java.sezpoz.Index
import net.java.sezpoz.Indexable

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
@Indexable(type = CommandFactory::class)
annotation class WaveCommandFactory

fun getAllCommandFactories(): List<CommandFactory> =
    Index.load(WaveCommandFactory::class.java, CommandFactory::class.java).map { it.instance() }.toList()