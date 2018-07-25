package com.github.dhsavell.wave.app

import picocli.CommandLine

fun main(args: Array<String>) {
    CommandLine.call(LauncherModule(), *args)
}