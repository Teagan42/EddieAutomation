package platform.models

sealed class Credentials {
    data class PasswordOnly(val password: String) : Credentials()
    data class Passsword(val userName: String,
                         val password: String
    ) : Credentials()

    data class Key(val key: String) : Credentials()
    data class ClientSecret(val clientId: String,
                            val secret: String
    ) : Credentials()
}