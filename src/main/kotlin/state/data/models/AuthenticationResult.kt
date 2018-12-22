package state.data.models

sealed class AuthenticationResult {
    object Authenticated : AuthenticationResult()

    open class Token(val token: String) : AuthenticationResult()

    class Refresh(token: String,
                  val refreshToken: String
    ) : Token(token)

    class Cookie(val dictionary: Map<String, String>) : AuthenticationResult()
}