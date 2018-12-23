package platform

interface Platform {
    val name: String
    val type: PlatformType

    suspend fun initialize()

    suspend fun destroy()
}

enum class PlatformType {
    POLL,
    PUSH
}