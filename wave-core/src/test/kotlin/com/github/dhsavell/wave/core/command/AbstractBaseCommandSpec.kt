package com.github.dhsavell.wave.core.command

import com.github.dhsavell.wave.core.command.impl.NumberRepeaterCommand
import com.github.dhsavell.wave.core.testutil.message
import com.github.dhsavell.wave.core.testutil.succeedWith
import com.xenomachina.argparser.UnexpectedPositionalArgumentException
import io.kotlintest.should
import io.kotlintest.shouldThrow
import io.kotlintest.specs.WordSpec
import org.dizitart.kno2.nitrite

class AbstractBaseCommandSpec : WordSpec({
    val db = nitrite { }

    "An AbstractBaseCommand" should {
        "be able to parse arguments" {
            NumberRepeaterCommand("number --number 1", message(), db).execute() should succeedWith("number is 1")
        }

        "throw an exception upon invalid arguments" {
            shouldThrow<UnexpectedPositionalArgumentException> {
                NumberRepeaterCommand("number invalid", message(), db).execute()
            }
        }
    }
})