package app

import app.bindings.AppBinding
import app.bindings.AppBinding.Companion.getInstance
import app.bindings.AppBindingArguments
import app.bindings.appModule
import config.ConfigLoader
import org.kodein.di.Kodein
import org.kodein.di.generic.instance
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

            configLoader.load().platforms.forEach {
                println(it)
            }
        }
    }
}