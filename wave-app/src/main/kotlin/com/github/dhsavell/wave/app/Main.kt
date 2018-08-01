package com.github.dhsavell.wave.app

import com.github.dhsavell.wave.app.provider.LauncherModule
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.mainBody

fun main(args: Array<String>) = mainBody {
    ArgParser(args).parseInto(::LauncherModule).createAndRunBot()
}