package app.bindings

import com.fasterxml.jackson.module.kotlin.KotlinModule
import common.SingletonHolder
import common.models.Credentials
import config.deserializers.CredentialsDeserializer
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

class AppBinding private constructor(private val args: Kodein) : Kodein by args {
    companion object : SingletonHolder<AppBinding, Kodein>(::AppBinding)
}

fun <CONFIG_SOURCE> appModule(args: AppBindingArguments<CONFIG_SOURCE>) =
    Kodein.Module {
        bind<KotlinModule>() with singleton {
            KotlinModule()
                .addDeserializer(Credentials::class.java,
                                 CredentialsDeserializer()
                )
                .let { it as KotlinModule }
        }
        import(configModule(args.configSource))
        import(platformModule())
    }

data class AppBindingArguments<CONFIG_SOURCE>(
        val configSource: CONFIG_SOURCE
)