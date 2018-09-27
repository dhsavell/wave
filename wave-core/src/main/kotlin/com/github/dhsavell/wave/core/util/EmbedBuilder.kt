package com.github.dhsavell.wave.core.util

import sx.blah.discord.api.internal.json.objects.EmbedObject
import sx.blah.discord.util.EmbedBuilder
import java.awt.Color

object StatusColors {
    val INFO = Color(0x45a1ff)
    val WARNING = Color(0xffe900)
    val ERROR = Color(0xff0039)
    val SUCCESS = Color(0x30e60b)
}

@DslMarker
annotation class EmbedDsl

@EmbedDsl
class DslEmbedBuilder {
    val builder = EmbedBuilder()

    fun title(init: () -> String) {
        builder.withTitle(init())
    }

    fun description(init: () -> String) {
        builder.withDescription(init())
    }

    fun color(init: () -> Color) {
        builder.withColor(init())
    }

    fun section(init: EmbedSectionBuilder.() -> Unit) {
        val section = EmbedSectionBuilder().apply(init)
        builder.appendField(section.title, section.description, section.inline)
    }

    fun footer(init: () -> String) {
        builder.withFooterText(init())
    }

    fun build(): EmbedObject {
        return builder.build()
    }

    @EmbedDsl
    class EmbedSectionBuilder {
        var title = ""
            private set
        var description = ""
            private set
        var inline = false
            private set

        fun title(init: () -> String) {
            title = init()
        }

        fun description(init: () -> String) {
            description = init()
        }

        fun notInline() {
            inline = false
        }

        fun inline() {
            inline = true
        }
    }
}

fun embed(init: DslEmbedBuilder.() -> Unit): EmbedObject = DslEmbedBuilder().apply(init).build()