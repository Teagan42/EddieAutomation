package config.model.platforms

import common.models.Credentials
import config.model.PlatformConfig
import java.net.URI

class ISYConfig(
        credentials: Credentials? = null,
        val deviceUUID: String,
        val deviceUri: URI
) : PlatformConfig(
        credentials
)