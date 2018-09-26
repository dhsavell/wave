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

    fun title(init: DslEmbedBuilder.() -> String) {
        builder.withTitle(init())
    }

    fun description(init: DslEmbedBuilder.() -> String) {
        builder.withDescription(init())
    }

    fun color(init: DslEmbedBuilder.() -> Color) {
        builder.withColor(init())
    }

    fun section(title: String, description: String, inline: Boolean = false) {
        builder.appendField(title, description, inline)
    }

    fun footer(init: DslEmbedBuilder.() -> String) {
        builder.withFooterText(init())
    }

    fun build(): EmbedObject {
        return builder.build()
    }
}

fun embed(init: DslEmbedBuilder.() -> Unit): EmbedObject = DslEmbedBuilder().apply(init).build()