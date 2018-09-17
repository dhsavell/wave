package com.github.dhsavell.wave.core.testutil

import com.github.dhsavell.wave.core.bot.WaveBot
import com.github.dhsavell.wave.core.command.Command
import org.mapdb.DBMaker
import org.slf4j.LoggerFactory
import sx.blah.discord.api.ClientBuilder

fun createDummyBot(commands: List<Command> = emptyList()) = WaveBot(
    ClientBuilder().withToken("this is an invalid token").build(),
    LoggerFactory.getLogger("wave-dummy"), "",
    DBMaker.memoryDB().make(),
    commands
)