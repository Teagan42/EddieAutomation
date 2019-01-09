package app

import app.bindings.*
import config.ConfigLoader
import config.model.PlatformConfig
import kotlinx.coroutines.*
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance
import org.kodein.di.generic.on
import platform.ISYPlatform
import platform.Platform
import platform.PlatformManager
import java.nio.file.Path

class App {
    companion object {
        lateinit var bindings: Kodein
        val coroutineScope: CoroutineScope = GlobalScope

        @JvmStatic
        fun main(args: Array<String>) {
            val source: Path = Path.of(args.first())
            bindings = getAppBindings(AppBindingArguments(source))

            val configLoader: ConfigLoader<Any> by bindings.on(ConfigScope).instance()
            val platformFactory by bindings.on(PlatformScope).factory<PlatformConfig, Platform>()

            coroutineScope.launch {
                configLoader.load()
                    .platforms
                    .map { platformFactory(it) }
                    .forEach {
                        initializePlatform(it)
                    }
            }

            while(true) {
                Thread.sleep(100)
            }

        }

        suspend fun initializePlatform(platform: Platform) = coroutineScope {
            platform.initialize()
        }
    }
}