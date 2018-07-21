package com.github.dhsavell.wave.app

import com.github.dhsavell.wave.app.command.meta.CommandHelp
import com.github.dhsavell.wave.core.bot.Bot
import com.github.dhsavell.wave.core.command.Command
import com.github.dhsavell.wave.core.command.CommandManager
import com.github.dhsavell.wave.core.conversation.ConversationManager
import mu.KotlinLogging
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import org.kodein.di.newInstance
import org.mapdb.DB
import org.mapdb.DBMaker
import org.slf4j.Logger
import picocli.CommandLine
import sx.blah.discord.api.ClientBuilder
import sx.blah.discord.api.IDiscordClient
import java.io.File
import java.util.concurrent.Callable

@CommandLine.Command(name = "wave")
class Launcher : Callable<Unit> {

    @CommandLine.Option(names = ["--token", "-t"], required = true, description = ["bot account token"])
    private
    lateinit var token: String

    @CommandLine.Option(names = ["--db-path", "-d"], description = ["db file path"])
    private var dbPath: File = File("./wave.mapdb")

    @CommandLine.Option(names = ["--use-memory-db", "-m"], description = ["use an in-memory database"])
    private var useMemoryDB: Boolean = false

    @CommandLine.Option(names = ["--prefix", "-p"], description = ["command prefix"])
    private var prefix = "w."

    override fun call() {
        val kodein = Kodein {
            bind<IDiscordClient>() with singleton {
                ClientBuilder()
                        .withToken(token)
                        .build()
            }

            bind<List<Command>>() with singleton {
                listOf(CommandHelp())
            }

            bind<DB>() with singleton {
                if (useMemoryDB) {
                    DBMaker.memoryDB().closeOnJvmShutdown().make()
                } else {
                    DBMaker.fileDB(dbPath).closeOnJvmShutdown().make()
                }
            }

            bind<Logger>() with provider {
                KotlinLogging.logger("wave")
            }

            bind<CommandManager>() with singleton { CommandManager(instance()) }
            bind<ConversationManager>() with singleton { ConversationManager() }
            bind<String>("prefix") with singleton { prefix }
        }

        val bot: Bot by kodein.newInstance { Bot(instance(), instance(), instance("prefix"), instance(), instance(), instance()) }
        bot.client.login()
    }
}

fun main(args: Array<String>) {
    CommandLine.call(Launcher(), *args)
}