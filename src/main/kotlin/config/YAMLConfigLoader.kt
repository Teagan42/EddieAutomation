package config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import config.model.Config
import java.nio.file.Files
import java.nio.file.Path

class YAMLConfigLoader(
        private val yamlFactory: YAMLFactory,
        override val source: Path
) : ConfigLoader<Path> {
    override fun load(): Config =
        ObjectMapper(yamlFactory)
            .apply { registerModule(KotlinModule()) }
            .let { mapper ->
                Files.newBufferedReader(source)
                    .use {
                        mapper.readValue(it,
                                         Config::class.java
                        )
                    }
            }
}