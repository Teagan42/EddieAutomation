package config.model

import common.models.Credentials

open class PlatformConfig(
        val name: String,
        val credentials: Credentials? = null
)