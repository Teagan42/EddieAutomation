package app.bindings

import app.App
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

private var appKodein: Kodein? = null

fun getAppBindings(args: AppBindingArguments) =
    appKodein ?: Kodein.lazy {
        import(
                getConfigKodein(ConfigScope,
                                args.source
                )
        )
        import(
                getPlatformKodein(PlatformScope)
        )

        bind<Kodein>() with singleton { App.bindings }
    }

data class AppBindingArguments(
        val source: Any
)