package config.mappers

import common.models.Credentials
import config.model.PlatformConfig
import config.model.platforms.ISYConfig
import data.remote.isy.ISYClient
import org.kodein.di.Kodein
import org.kodein.di.generic.instance
import platform.ISYPlatform
import platform.Platform

class PlatformMapper(private val kodein: Kodein) : (PlatformConfig) -> Platform {
    override fun invoke(config: PlatformConfig): Platform =
        when (config) {
            is ISYConfig -> {
                val client: ISYClient by kodein.instance()

                ISYPlatform(config.credentials as Credentials.Passsword,
                            client
                )
            }
            else -> throw IllegalArgumentException("Unknown platform ${config.javaClass.simpleName}")
        }
}