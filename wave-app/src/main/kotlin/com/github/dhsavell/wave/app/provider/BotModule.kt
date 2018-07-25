package com.github.dhsavell.wave.app.provider

import com.github.dhsavell.wave.core.bot.Bot
import com.github.dhsavell.wave.core.command.CommandManager
import com.github.dhsavell.wave.core.conversation.ConversationManager
import com.github.dhsavell.wave.core.permission.PermissionManager
import dagger.Module
import dagger.Provides
import org.mapdb.DB
import org.slf4j.Logger
import sx.blah.discord.api.ClientBuilder
import sx.blah.discord.api.IDiscordClient
import javax.inject.Named

@Module
class BotModule {
    @Provides
    fun provideClient(@Named("token") token: String): IDiscordClient {
        return ClientBuilder().withToken(token).withRecommendedShardCount().build()
    }

    @Provides
    fun provideBot(client: IDiscordClient,
                   logger: Logger,
                   @Named("prefix") prefix: String,
                   db: DB,
                   commandManager: CommandManager,
                   conversationManager: ConversationManager,
                   permissionManager: PermissionManager): Bot {
        return Bot(client, logger, prefix, db, commandManager, conversationManager, permissionManager)
    }
}