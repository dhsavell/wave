package com.github.dhsavell.wave.core.command

import com.xenomachina.argparser.ArgParser

interface CommandFactory {
    fun getAction(parser: ArgParser)
}