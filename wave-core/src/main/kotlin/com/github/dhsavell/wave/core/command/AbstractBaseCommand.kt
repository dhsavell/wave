package com.github.dhsavell.wave.core.command

import com.xenomachina.argparser.ArgParser
import sx.blah.discord.handle.obj.IMessage

/**
 * A utility class providing common Command functionality, such as an argument parser and getters for frequently-used
 * data (i.e. the server the command was called in).
 */
abstract class AbstractBaseCommand(val context: IMessage) : Command {
    protected val args = ArgParser(context.content.split(" ").toTypedArray())

    val caller
        get() = context.author

    val channel
        get() = context.channel

    val server
        get() = context.guild

    val client
        get() = context.client
}