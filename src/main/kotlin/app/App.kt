package app

import app.bindings.*
import config.ConfigLoader
import config.model.PlatformConfig
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance
import org.kodein.di.generic.on
import platform.Platform
import java.nio.file.Path

class App {
    companion object {
        lateinit var bindings: Kodein

        @JvmStatic
        fun main(args: Array<String>) {
            val source: Path = Path.of(args.first())
            bindings = getAppBindings(AppBindingArguments(source))

            val configLoader: ConfigLoader<Any> by bindings.on(ConfigScope).instance()
            val platformFactory by bindings.on(PlatformScope).factory<PlatformConfig, Platform>()

            configLoader.load()
                .platforms.forEach { platformConfig ->
                    platformFactory(platformConfig).let {
                        println("PlatformConfig: $platformConfig")
                        println("Platform: ${it}")
                    }

            }
        }
    }
}