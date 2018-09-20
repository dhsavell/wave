package com.github.dhsavell.wave.core.command

import com.github.dhsavell.wave.core.command.impl.NumberRepeaterCommand
import com.github.dhsavell.wave.core.testutil.stubMessage
import com.xenomachina.argparser.UnexpectedPositionalArgumentException
import io.kotlintest.matchers.beInstanceOf
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.WordSpec

class AbstractBaseCommandSpec : WordSpec({
    "An AbstractBaseCommand" should {
        "be able to parse arguments" {
            val result = NumberRepeaterCommand(
                "number --number 1",
                stubMessage()
            ).execute()

            result should beInstanceOf(CommandSucceededWithValue::class)
            (result as CommandSucceededWithValue).result shouldBe "number is 1"
        }

        "throw an exception upon invalid arguments" {
            shouldThrow<UnexpectedPositionalArgumentException> {
                NumberRepeaterCommand("number invalid", stubMessage()).execute()
            }
        }
    }
})