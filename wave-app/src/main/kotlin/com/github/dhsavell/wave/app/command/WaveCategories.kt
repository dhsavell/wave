package com.github.dhsavell.wave.app.command

import com.github.dhsavell.wave.core.command.Category

sealed class WaveCategory(override val name: String, override val description: String) : Category

object Meta : WaveCategory("meta", "Commands for information about the bot itself.")
object Settings : WaveCategory("settings", "Commands for configuring the bot.")