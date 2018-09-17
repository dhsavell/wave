package com.github.dhsavell.wave.app

import com.github.dhsavell.wave.app.command.CommandProvider
import com.github.dhsavell.wave.core.bot.WaveBot
import com.github.dhsavell.wave.core.command.Command
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import mu.KotlinLogging
import net.java.sezpoz.Index
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import org.kodein.di.newInstance
import org.mapdb.DB
import org.mapdb.DBMaker
import org.slf4j.Logger
import sx.blah.discord.api.ClientBuilder
import sx.blah.discord.api.IDiscordClient
import java.io.File

class WaveLauncher(parser: ArgParser) {
    private val token by parser.storing("token to run the bot with")
    private val dbPath by parser.storing("path to the database") { File(this) }.default(File("./wave.mapdb"))
    private val useMemoryDB by parser.flagging(
        "--use-memory-db",
        help = "whether or not data should be kept in memory"
    ).default(false)
    private val prefix by parser.storing("bot command prefix").default("w.")

    private val kodein: Kodein = Kodein {
        bind<DB>() with singleton {
            if (useMemoryDB) {
                DBMaker.memoryDB().make()
            } else {
                DBMaker.fileDB(dbPath).make()
            }
        }

        bind<String>(tag = "prefix") with singleton { prefix }
        bind<Logger>() with provider { KotlinLogging.logger("wave") }
        bind<IDiscordClient>() with provider { ClientBuilder().withToken(token).withRecommendedShardCount().build() }
        bind<List<Command>>() with provider {
            Index.load(CommandProvider::class.java, Command::class.java).map { indexItem -> indexItem.instance() }
        }
    }

    fun createAndRunBot() {
        val bot by kodein.newInstance { WaveBot(instance(), instance(), instance("prefix"), instance(), instance()) }
        bot.runForever()
    }
}