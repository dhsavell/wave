package com.github.dhsavell.wave.core.util

import sx.blah.discord.api.internal.json.objects.EmbedObject
import sx.blah.discord.util.EmbedBuilder
import java.awt.Color

class DslEmbedBuilder internal constructor(initBlock: DslEmbedBuilder.() -> Unit) {
    init {
        initBlock()
    }

    var builder = EmbedBuilder()

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

fun embed(initBlock: DslEmbedBuilder.() -> Unit): EmbedObject = DslEmbedBuilder(initBlock).build()