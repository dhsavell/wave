package com.github.dhsavell.wave.app.provider

import com.github.dhsavell.wave.core.command.CommandAction
import com.github.dhsavell.wave.core.conversation.ConversationManager
import com.github.dhsavell.wave.core.permission.PermissionManager
import dagger.Module
import dagger.Provides
import net.java.sezpoz.Index
import net.java.sezpoz.Indexable
import org.mapdb.DB

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Indexable(type = CommandAction::class)
annotation class CommandProvider

@Module
class ManagerModule {
    @Provides
    fun provideCommandManager(): CommandManager {
        return CommandManager(
            Index.load(
                CommandProvider::class.java,
                CommandAction::class.java
            ).map { indexItem -> indexItem.instance() })
    }

    @Provides
    fun provideConversationManager(): ConversationManager {
        return ConversationManager()
    }

    @Provides
    fun providePermissionManager(db: DB): PermissionManager {
        return PermissionManager(db)
    }
}