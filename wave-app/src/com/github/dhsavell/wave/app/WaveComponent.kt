package com.github.dhsavell.wave.app

import com.github.dhsavell.wave.core.bot.Bot
import dagger.Component

@Component
interface WaveComponent {
    fun bot(): Bot
}