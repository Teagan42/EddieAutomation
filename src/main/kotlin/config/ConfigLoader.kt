package config

import config.model.Config

interface ConfigLoader<SOURCE> {
    val source: SOURCE
    fun load() : Config
}