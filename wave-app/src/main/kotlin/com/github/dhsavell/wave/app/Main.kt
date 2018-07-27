package com.github.dhsavell.wave.app

import com.github.dhsavell.wave.app.provider.LauncherModule
import picocli.CommandLine

fun main(args: Array<String>) {
    CommandLine.call(LauncherModule(), *args)
}