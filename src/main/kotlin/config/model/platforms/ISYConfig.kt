package config.model.platforms

import common.models.Credentials
import config.model.PlatformConfig
import java.net.URI
import java.util.*

class ISYConfig(
        name: String,
        credentials: Credentials? = null,
        val deviceUUID: String,
        val deviceUri: URI
) : PlatformConfig(
        name,
        credentials
)