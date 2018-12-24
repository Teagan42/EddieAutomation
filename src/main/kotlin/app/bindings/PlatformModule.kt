package app.bindings

import config.mappers.PlatformMapper
import config.model.PlatformConfig
import config.model.platforms.ISYConfig
import data.remote.isy.ISYClient
import data.remote.isy.ISYRemoteClient
import kotlinx.coroutines.channels.Channel
import org.kodein.di.Kodein
import org.kodein.di.generic.*
import org.yaml.snakeyaml.events.NodeEvent
import platform.Platform

fun isyModule() =
    Kodein.Module {
        bind<Channel<NodeEvent>>() with provider { Channel<NodeEvent>() }
        bind<ISYClient>() with factory { config: ISYConfig ->
            ISYRemoteClient(config.deviceUUID,
                            config.deviceUri,
                            instance()
            )
        }
    }

fun platformModule() =
    Kodein.Module {
        isyModule()
            .also { bind<Kodein>() with singleton { Kodein {import(it)} } }
            .also { import(it) }

        bind<Platform>() with factory { config: PlatformConfig -> PlatformMapper(instance())(config) }
    }