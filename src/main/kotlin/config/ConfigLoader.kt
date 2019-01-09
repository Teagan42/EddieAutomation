package config

import config.model.Config

interface ConfigLoader<SOURCE : Any> {
    val source: SOURCE
    fun load(): Config
}