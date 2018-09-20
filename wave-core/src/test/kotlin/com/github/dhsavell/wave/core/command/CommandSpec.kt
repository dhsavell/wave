package com.github.dhsavell.wave.core.command

import com.github.dhsavell.wave.core.testutil.messageWithContent
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class CommandSpec : StringSpec({
    "A command can be called with arguments" {
        NumberRepeaterCommand(messageWithContent("--number 1")).execute() shouldBe CommandSuccededWithValue("number is 1")
    }
})