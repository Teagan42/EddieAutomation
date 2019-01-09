package app.bindings

import app.bindings.PlatformScope.TAG_COROUTINE_CONTEXT
import app.bindings.PlatformScope.TAG_CREDENTIALS
import app.bindings.PlatformScope.TAG_DEVICE_URI
import app.bindings.PlatformScope.TAG_DEVICE_UUID
import common.models.Credentials
import config.mappers.PlatformMapper
import config.model.Config
import config.model.PlatformConfig
import config.model.platforms.ISYConfig
import data.models.ISYNodeEvent
import data.remote.isy.ISYClient
import data.remote.isy.ISYRemoteClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import org.kodein.di.Kodein
import org.kodein.di.bindings.ScopeCloseable
import org.kodein.di.bindings.SubScope
import org.kodein.di.bindings.subTypes
import org.kodein.di.generic.*
import org.kodein.di.jvmType
import platform.ISYPlatform
import platform.Platform
import java.net.URI
import kotlin.coroutines.CoroutineContext

object UnknownPlatformConfig : PlatformConfig()

object PlatformScope : SubScope<PlatformScope, ConfigScope>(ConfigScope),
                       ScopeCloseable {
    const val TAG_DEVICE_UUID = "ISY.deviceUUID"
    const val TAG_DEVICE_URI = "ISY.deviceUri"
    const val TAG_CREDENTIALS = "ISY.credentials"
    const val TAG_COROUTINE_CONTEXT = "ISY.coroutineContext"

    override fun close() {
        getRegistry(this).clear()
    }

    override fun getParentContext(context: PlatformScope): ConfigScope = ConfigScope
}

fun getISYKodein(scope: PlatformScope) =
    Kodein.Module {
        bind<ISYConfig>() with scoped(scope).singleton {
            instance<Config>()
                .platforms.first { it is ISYConfig }
                .let { it as ISYConfig }
        }
        bind<String>(TAG_DEVICE_UUID) with scoped(scope).singleton {
            on(scope).instance<ISYConfig>()
                .deviceUUID
        }
        bind<URI>(TAG_DEVICE_URI) with scoped(scope).singleton {
            on(scope).instance<ISYConfig>()
                .deviceUri
        }
        bind<Credentials.Passsword>(TAG_CREDENTIALS) with scoped(scope).singleton { on(scope).instance<ISYConfig>().credentials as Credentials.Passsword }
        bind<Channel<ISYNodeEvent>>() with scoped(scope).singleton { Channel<ISYNodeEvent>() }
        bind<CoroutineContext>(TAG_COROUTINE_CONTEXT) with scoped(scope).singleton { Dispatchers.IO + Job() }
        bind<ISYClient>() with scoped(scope).singleton {
            ISYRemoteClient(on(scope).instance(TAG_DEVICE_UUID),
                            on(scope).instance(TAG_DEVICE_URI),
                            on(scope).instance(),
                            on(scope).instance(TAG_COROUTINE_CONTEXT)
            )
        }
        bind<ISYPlatform>() with scoped(scope).singleton {
            ISYPlatform(instance(),
                        instance()
            )
        }
    }

fun getPlatformLoader(scope: PlatformScope) =
    Kodein.Module {
        bind<PlatformMapper>() with scoped(scope).singleton { PlatformMapper(kodein) }
        bind<PlatformConfig>().subTypes() with { type ->
            when (type.jvmType) {
                ISYConfig::class.java -> scoped(scope).singleton {
                    ISYConfig(on(scope).instance(TAG_CREDENTIALS),
                              on(scope).instance(TAG_DEVICE_UUID),
                              on(scope).instance(TAG_DEVICE_URI)
                    )
                }
                else -> scoped(scope).singleton { UnknownPlatformConfig }
            }
        }

        bind<Platform>() with factory { platformConfig: PlatformConfig ->
            on(scope).instance<PlatformMapper>()(platformConfig)
        }
    }

fun getPlatformKodein(scope: PlatformScope) =
    Kodein.Module {
        import(getPlatformLoader(scope))
        import(getISYKodein(scope))
    }