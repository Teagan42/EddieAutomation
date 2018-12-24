package app.bindings

import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import config.ConfigLoader
import config.YAMLConfigLoader
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import java.nio.file.Path

fun <CONFIG_SOURCE> configModule(args: CONFIG_SOURCE) =
    Kodein.Module {
        bind<Path>() with singleton { args as Path }
        bind<YAMLFactory>() with singleton { YAMLFactory() }
        bind<ConfigLoader<*>>() with singleton {
            YAMLConfigLoader(instance(),
                             instance(),
                             instance()
            )
        }
    }