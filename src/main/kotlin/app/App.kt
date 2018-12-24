package app

import app.bindings.AppBinding
import app.bindings.AppBinding.Companion.getInstance
import app.bindings.AppBindingArguments
import app.bindings.appModule
import config.ConfigLoader
import config.model.PlatformConfig
import org.kodein.di.Kodein
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import platform.Platform
import java.nio.file.Path

class App {
    companion object {
        lateinit var bindings: AppBinding

        @JvmStatic
        fun main(args: Array<String>) {
            bindings = getInstance(
                    Kodein { import(appModule(AppBindingArguments(Path.of(args.first())))) }
            )

            val configLoader: ConfigLoader<*> by bindings.instance()
            val platformFactory: (PlatformConfig) -> Platform by bindings.factory()

            configLoader.load().platforms.forEach {
                val platform = platformFactory(it)

                println("Platform: ${platform.javaClass.simpleName}")
            }
        }
    }
}