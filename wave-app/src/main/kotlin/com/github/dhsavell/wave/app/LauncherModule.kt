package com.github.dhsavell.wave.app

import com.github.dhsavell.wave.app.provider.BotModule
import com.github.dhsavell.wave.app.provider.DaggerBotComponent
import com.github.dhsavell.wave.app.provider.ManagerModule
import dagger.Module
import dagger.Provides
import mu.KotlinLogging
import org.mapdb.DB
import org.mapdb.DBMaker
import org.slf4j.Logger
import picocli.CommandLine
import java.io.File
import java.util.concurrent.Callable
import javax.inject.Named

@Module
@CommandLine.Command(name = "wave")
class LauncherModule : Callable<Unit> {
    @CommandLine.Option(names = ["--token", "-t"], required = true, description = ["bot account token"])
    private lateinit var token: String

    @CommandLine.Option(names = ["--db-path", "-d"], description = ["db file path"])
    private var dbPath: File = File("./wave.mapdb")

    @CommandLine.Option(names = ["--use-memory-db", "-m"], description = ["use an in-memory database"])
    private var useMemoryDB: Boolean = false

    @CommandLine.Option(names = ["--prefix", "-p"], description = ["command prefix"])
    private var prefix = "w."

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

    override fun call() {
        val bot = DaggerBotComponent.builder()
                .botModule(BotModule())
                .managerModule(ManagerModule())
                .launcherModule(this)
                .build()
                .bot()

        bot.runForever()
    }
}