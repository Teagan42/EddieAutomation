package config.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeId
import com.fasterxml.jackson.annotation.JsonTypeInfo
import common.models.Credentials
import config.model.platforms.ISYConfig

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
              include = JsonTypeInfo.As.PROPERTY,
              property = "platform"
)
@JsonSubTypes(
        value = [
            JsonSubTypes.Type(ISYConfig::class,
                              name = "ISY"
            )
        ]
)
open class PlatformConfig(
        val credentials: Credentials? = null
) {
    @JsonTypeId lateinit var platform: String
}