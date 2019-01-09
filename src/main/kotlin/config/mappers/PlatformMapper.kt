package config.mappers

import app.bindings.PlatformScope
import common.models.Credentials
import config.model.PlatformConfig
import config.model.platforms.ISYConfig
import data.remote.isy.ISYClient
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.KodeinContext
import org.kodein.di.direct
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext
import org.kodein.di.generic.on
import platform.ISYPlatform
import platform.Platform

class PlatformMapper(override val kodein: Kodein) : KodeinAware, (PlatformConfig) -> Platform {

    override fun invoke(config: PlatformConfig): Platform =
        when (config) {
            is ISYConfig -> {
                val client: ISYClient = direct.on(PlatformScope).instance()

                ISYPlatform(config.credentials as Credentials.Passsword,
                            client
                )
            }
            else -> throw IllegalArgumentException("Unknown platform ${config.javaClass.simpleName}")
        }
}