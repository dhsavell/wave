package com.github.dhsavell.wave.app.provider

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import dagger.Module
import dagger.Provides
import mu.KotlinLogging
import org.mapdb.DB
import org.mapdb.DBMaker
import org.slf4j.Logger
import sx.blah.discord.api.ClientBuilder
import sx.blah.discord.api.IDiscordClient
import java.io.File
import javax.inject.Named

@Module
class LauncherModule(parser: ArgParser) {
    private val token by parser.storing("token to run the bot with")
    private val dbPath by parser.storing("path to the database") { File(this) }.default(File("./wave.mapdb"))
    private val useMemoryDB by parser.flagging("--use-memory-db", help = "whether or not data should be kept in memory").default(false)
    private val prefix by parser.storing("bot command prefix").default("w.")

    @Provides
    @Named("token")
    fun provideToken(): String {
        return token
    }

    @Provides
    @Named("prefix")
    fun providePrefix(): String {
        return prefix
    }

    @Provides
    fun provideDatabase(): DB {
        return if (useMemoryDB) {
            DBMaker.memoryDB().make()
        } else {
            DBMaker.fileDB(dbPath).make()
        }
    }

    @Provides
    fun provideLogger(): Logger {
        return KotlinLogging.logger("wave")
    }

    @Provides
    fun provideClient(@Named("token") token: String): IDiscordClient {
        return ClientBuilder().withToken(token).withRecommendedShardCount().build()
    }

    fun createAndRunBot() {
        val bot = DaggerBotComponent.builder()
                .managerModule(ManagerModule())
                .launcherModule(this)
                .build()
                .bot()

        bot.runForever()
    }
}