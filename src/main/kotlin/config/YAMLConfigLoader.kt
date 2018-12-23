package config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import config.model.Config
import java.nio.file.Files
import java.nio.file.Path

class YAMLConfigLoader(
        private val yamlFactory: YAMLFactory,
        private val kotlinModule: KotlinModule,
        override val source: Path
) : ConfigLoader<Path> {
    override fun load(): Config =
        ObjectMapper(yamlFactory)
            .configure(DeserializationFeature.FAIL_ON_MISSING_EXTERNAL_TYPE_ID_PROPERTY,
                       false
            )
            .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY,
                       true
            )
            .configure(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS,
                       true
            )
            .registerModule(kotlinModule)
            .let { mapper ->
                Files.newBufferedReader(source)
                    .use {
                        mapper.readValue(it,
                                         Config::class.java
                        )
                    }
            }
}