package com.github.dhsavell.wave.app

import com.github.dhsavell.wave.core.bot.Bot
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.mainBody
import mu.KotlinLogging
import org.mapdb.DBMaker
import sx.blah.discord.api.ClientBuilder

class Launcher(parser: ArgParser) {
    private val useMemoryDB by parser.flagging("use-memory-db")
    val token by parser.storing("token", "t")
    val prefix by parser.storing("prefix", "p")

    fun runBot() {
        val db = if (useMemoryDB) { DBMaker.memoryDB().make() } else { DBMaker.fileDB("./wave.db").make() }
        val client = ClientBuilder().withToken(token).build()
        val bot = Bot(client, KotlinLogging.logger { }, prefix, db, getAllCommandFactories())

        bot.runForever()
    }
}

fun main(args: Array<String>) = mainBody {
    ArgParser(args).parseInto(::Launcher).runBot()
}