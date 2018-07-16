package com.github.dhsavell.wave.app.provider

import com.github.dhsavell.wave.core.command.Command
import com.github.dhsavell.wave.core.command.CommandManager
import dagger.Module
import dagger.Provides
import net.java.sezpoz.Index
import net.java.sezpoz.Indexable
import javax.inject.Inject

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Indexable(type = Command::class)
annotation class LoadCommand

@Module
object CommandModule {
    @Provides
    fun provideCommands(): List<Command> {
        return Index.load(LoadCommand::class.java, Command::class.java).mapNotNull { it.instance() }
    }

    @Provides
    @Inject
    fun provideCommandManager(commands: List<Command>): CommandManager {
        return CommandManager(commands)
    }
}