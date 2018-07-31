package com.github.dhsavell.wave.app

import com.github.dhsavell.wave.app.provider.LauncherModule
import com.xenomachina.argparser.ArgParser

fun main(args: Array<String>) = ArgParser(args).parseInto(::LauncherModule).createAndRunBot()