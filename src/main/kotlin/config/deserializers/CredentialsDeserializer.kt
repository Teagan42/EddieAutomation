package config.deserializers

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import common.models.Credentials

class CredentialsDeserializer : JsonDeserializer<Credentials?>() {
    override fun deserialize(jsonParser: JsonParser?,
                             ctxt: DeserializationContext?
    ): Credentials? =
        jsonParser?.codec?.readTree<JsonNode>(jsonParser)
            ?.let {
                when {
                    it.has("key") ->
                        Credentials.Key(it["key"].asText())
                    it.has("clientId") ->
                        Credentials.ClientSecret(
                                it["clientId"].asText(),
                                it["secret"].asText()
                        )
                    it.has("username") ->
                        Credentials.Passsword(
                                it["username"].asText(),
                                it["password"].asText()
                        )
                    it.has("password") ->
                        Credentials.PasswordOnly(
                                it["password"].asText()
                        )
                    else -> null
                }
            }
}