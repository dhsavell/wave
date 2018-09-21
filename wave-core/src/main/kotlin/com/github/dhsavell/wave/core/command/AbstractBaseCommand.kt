package com.github.dhsavell.wave.core.command

import com.xenomachina.argparser.ArgParser
import org.dizitart.no2.Nitrite
import sx.blah.discord.handle.obj.IMessage

/**
 * A utility class providing common Command functionality, such as an argument parser and getters for frequently-used
 * data (i.e. the server the command was called in).
 */
abstract class AbstractBaseCommand(val commandCall: String, val context: IMessage, val db: Nitrite) : Command {
    protected val args = ArgParser(commandCall.split(" ").drop(1).toTypedArray())

    /**
     * User that called this command.
     */
    val caller
        get() = context.author

    /**
     * Channel this command was called in.
     */
    val channel
        get() = context.channel

    /**
     * Server this command was called in.
     */
    val server
        get() = context.guild

    /**
     * Discord client that received this command.
     */
    val client
        get() = context.client
}