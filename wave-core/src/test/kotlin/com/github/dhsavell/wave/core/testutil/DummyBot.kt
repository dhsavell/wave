package com.github.dhsavell.wave.core.testutil

import com.github.dhsavell.wave.core.bot.Bot
import com.github.dhsavell.wave.core.command.Command
import com.github.dhsavell.wave.core.command.CommandManager
import com.github.dhsavell.wave.core.conversation.ConversationManager
import com.github.dhsavell.wave.core.permission.PermissionManager
import org.mapdb.DB
import org.mapdb.DBMaker
import org.slf4j.LoggerFactory
import sx.blah.discord.api.ClientBuilder

class DummyBot(commands: List<Command> = emptyList(), db: DB = DBMaker.memoryDB().make()) :
    Bot(
        ClientBuilder().withToken("this is an invalid token").build(),
        LoggerFactory.getLogger(DummyBot::class.java), "",
        DBMaker.memoryDB().make(),
        CommandManager(commands),
        ConversationManager(),
        PermissionManager(db)
    )