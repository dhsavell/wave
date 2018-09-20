package com.github.dhsavell.wave.core.command

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class CommandSpec : StringSpec({
    "A command can be called with arguments" {
        NumberRepeaterCommand().execute() shouldBe CommandSucceededWithValue("number is 1")
    }
})