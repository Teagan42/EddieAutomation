package app.bindings

import app.bindings.Tags.TAG_CONFIG_SOURCE
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import common.models.Credentials
import config.ConfigLoader
import config.YAMLConfigLoader
import config.deserializers.CredentialsDeserializer
import config.model.Config
import org.kodein.di.Kodein
import org.kodein.di.bindings.Scope
import org.kodein.di.bindings.ScopeCloseable
import org.kodein.di.bindings.ScopeRegistry
import org.kodein.di.bindings.StandardScopeRegistry
import org.kodein.di.generic.*
import java.nio.file.Path

object Tags {
    const val TAG_CONFIG_SOURCE = "configSource"
}

object ConfigScope : Scope<ConfigScope>,
                     ScopeCloseable {

    private val registry = StandardScopeRegistry()

    override fun getRegistry(context: ConfigScope): ScopeRegistry =
        context.registry

    override fun close() {
        registry.clear()
    }
}

fun getConfigKodein(configScope: ConfigScope,
                    contextSource: Any
) = Kodein.Module {
    bind<Any>(TAG_CONFIG_SOURCE) with singleton { contextSource }
    bind<KotlinModule>() with scoped(configScope).singleton {
        KotlinModule()
            .addDeserializer(Credentials::class.java,
                             CredentialsDeserializer()
            )
            .let { it as KotlinModule }
    }
    bind<Path>() with scoped(configScope).singleton { instance<Any>(TAG_CONFIG_SOURCE) as Path }
    bind<YAMLFactory>() with scoped(configScope).singleton { YAMLFactory() }
    bind<ConfigLoader<Any>>() with scoped(configScope).singleton {
        YAMLConfigLoader(instance(),
                         instance(),
                         instance()
        ).let { it as ConfigLoader<Any> }
    }
    bind<Config>() with singleton {
        YAMLConfigLoader(on(configScope).instance(),
                         on(configScope).instance(),
                         on(configScope).instance()
        ).load()
    }
}